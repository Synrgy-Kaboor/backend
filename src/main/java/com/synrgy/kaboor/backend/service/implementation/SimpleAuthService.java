package com.synrgy.kaboor.backend.service.implementation;

import com.synrgy.kaboor.backend.dto.request.*;
import com.synrgy.kaboor.backend.dto.response.LoginDtoResponse;
import com.synrgy.kaboor.backend.dto.response.OtpDtoResponse;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;
import com.synrgy.kaboor.backend.model.Role;
import com.synrgy.kaboor.backend.model.Token;
import com.synrgy.kaboor.backend.model.TokenType;
import com.synrgy.kaboor.backend.model.User;
import com.synrgy.kaboor.backend.repository.TokenRepository;
import com.synrgy.kaboor.backend.repository.UserRepository;
import com.synrgy.kaboor.backend.security.JwtService;
import com.synrgy.kaboor.backend.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleAuthService implements AuthService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public RegisterUserDtoResponse registerAsUser(RegisterUserDtoRequest registerUserDtoRequest) {
        // TODO: Handle exception

        // check if user with some email already exist
        User existingUser = userRepository.findByEmail(registerUserDtoRequest.getEmail()).orElse(null);
        if (existingUser != null) {
            throw new RuntimeException("User with email " + registerUserDtoRequest.getEmail() + " already existed!");
        }

        User user = User.builder()
                .fullName(registerUserDtoRequest.getFullName())
                .phoneNumber(registerUserDtoRequest.getPhoneNumber())
                .email(registerUserDtoRequest.getEmail())
                .password(passwordEncoder.encode(registerUserDtoRequest.getPassword()))
                .verifyDeadlines(getNextFiveMinutesOnSeconds())
                .role(Role.USER)
                .build();

        // generate otp and set to user model
        user.setOtp(generateOtp());

        User newUser = userRepository.save(user);

        // send otp to email for verification
        sendVerificationEmail(newUser.getEmail(), newUser.getOtp());

        return RegisterUserDtoResponse.builder()
                .fullName(newUser.getFullName())
                .email(newUser.getEmail())
                .phoneNumber(newUser.getPhoneNumber())
                .nextFiveMinutesOnSeconds(newUser.getVerifyDeadlines())
                .verified(newUser.isVerified())
                .build();
    }

    @Override
    public OtpDtoResponse verifyOtp(OtpDtoRequest otpDtoRequest) {
        // TODO: Handle exceptions
        //

        // Find OTP from User table
        User user = userRepository.findByOtp(otpDtoRequest.getOtp())
                .orElseThrow(() -> new RuntimeException("OTP not valid for any users!"));

        // Compare OTP deadlines with current time
        if (getCurrentSeconds() - user.getVerifyDeadlines() >= 300) {
            throw new RuntimeException("OTP has expired!");
        }

        // Update verified status on User => true
        user.setVerified(true);
        User updatedUser = userRepository.save(user);

        return OtpDtoResponse.builder()
                .email(updatedUser.getEmail())
                .fullName(updatedUser.getFullName())
                .phoneNumber(updatedUser.getPhoneNumber())
                .verified(updatedUser.isVerified())
                .build();
    }

    @Override
    public LoginDtoResponse login(LoginDtoRequest loginDtoRequest) {
        // TODO: Handle exceptions

        // Find user by email
        User user = userRepository.findByEmail(loginDtoRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User with email " + loginDtoRequest.getEmail() + " not found!"));

        // Check if user not verified
        if (!user.isVerified()) {
            throw new RuntimeException("User not verified, please verify with email OTP first!");
        }

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDtoRequest.getEmail(),
                        loginDtoRequest.getPassword()
                )
        );

        // Generate JWT
        String jwt = jwtService.generateToken(user);

        Token token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .build();

        tokenRepository.save(token);

        // Revoke all existing user tokens from DB
        revokeAllUserTokens(user);

        // Save new token to DB
        tokenRepository.save(token);

        return LoginDtoResponse.builder()
                .jwt(jwt)
                .role(user.getRole())
                .build();
    }

    @Override
    public void resendOtp(ResendRequestDto resendRequestDto) {
        // Find user by email
        User user = userRepository.findByEmail(resendRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User with email " + resendRequestDto.getEmail() + " not found!"));

        // Check whether the user has just sent the previous OTP
        // Prevent spam
        if (getCurrentSeconds() - user.getVerifyDeadlines() < 300) {
            throw new RuntimeException("New OTP just sent to email " + user.getEmail() + ", please check it first!");
        }

        String otp = generateOtp();
        sendVerificationEmail(user.getEmail(), otp);

        // Update OTP and OTP deadline on User table
        user.setOtp(otp);
        user.setVerifyDeadlines(getNextFiveMinutesOnSeconds());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        // TODO: Check if OTP that verified before was for change password

        // Find user by email
        User user = userRepository.findByEmail(changePasswordRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User with email " + changePasswordRequestDto.getEmail() + " not found!"));

        // Update password
        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
        userRepository.save(user);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(token -> token.setExpired(true));
        tokenRepository.saveAll(validTokens);
    }

    private long getNextFiveMinutesOnSeconds() {
        long currentSeconds = getCurrentSeconds();
        return currentSeconds + 300;
    }

    private long getCurrentSeconds() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendVerificationEmail(String emailTo, String otp) {
        String subject = "Kaboor Email Verification";
        String body = "Your verification OTP: " + otp;
        sendEmail(emailTo, subject, body);
    }

    private void sendEmail(String to, String subject, String messageBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(messageBody, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error when sending an email message!");
        }
    }

}
