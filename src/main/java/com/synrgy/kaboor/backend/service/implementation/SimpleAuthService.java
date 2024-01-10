package com.synrgy.kaboor.backend.service.implementation;

import com.synrgy.kaboor.backend.dto.request.RegisterUserDtoRequest;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;
import com.synrgy.kaboor.backend.model.Role;
import com.synrgy.kaboor.backend.model.User;
import com.synrgy.kaboor.backend.repository.UserRepository;
import com.synrgy.kaboor.backend.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleAuthService implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Override
    public RegisterUserDtoResponse registerAsUser(RegisterUserDtoRequest registerUserDtoRequest) {
        // check if user with some email already exist
        User existingUser = userRepository.findByEmail(registerUserDtoRequest.getEmail()).orElse(null);
        if (existingUser != null) {
            throw new RuntimeException("User with email " + registerUserDtoRequest.getEmail() + " already existed!");
        }

        log.info("CURRENT [{}]", getCurrentSeconds());
        log.info("NEXT FIVE MINUTES [{}]", getNextFiveMinutesOnSeconds());

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
                .verified(newUser.isVerified())
                .build();
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
