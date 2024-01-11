package com.synrgy.kaboor.backend.controller;

import com.synrgy.kaboor.backend.dto.request.*;
import com.synrgy.kaboor.backend.dto.response.LoginDtoResponse;
import com.synrgy.kaboor.backend.dto.response.OtpDtoResponse;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;
import com.synrgy.kaboor.backend.service.AuthService;
import com.synrgy.kaboor.backend.util.BaseResponse;
import com.synrgy.kaboor.backend.util.BaseResponseWithoutData;
import com.synrgy.kaboor.backend.util.ResponseUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.synrgy.kaboor.backend.util.MessageResponse.*;

@Tag(name = "Authentication Resource")
@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final Map<String, Object> dataMap = new HashMap<>();

    @Operation(summary = "Register as user (customer)")
    @PostMapping("/register/user")
    public ResponseEntity<BaseResponse<Map<String, Object>>> registerAsUser(@Valid @RequestBody RegisterUserDtoRequest request) {
        RegisterUserDtoResponse data = authService.registerAsUser(request);

        dataMap.clear();
        dataMap.put("user", data);

        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        response.setCode(HttpStatus.CREATED.value());
        response.setMessage(NEW_USER_CREATED_MESSAGE);
        response.setData(dataMap);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Verify OTP")
    @PostMapping("/otp/verify")
    public ResponseEntity<BaseResponse<Map<String, Object>>> verifyOtp(@Valid @RequestBody OtpDtoRequest otpDtoRequest) {
        OtpDtoResponse otpDtoResponse = authService.verifyOtp(otpDtoRequest);

        dataMap.clear();
        dataMap.put("user", otpDtoResponse);

        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(USER_IS_VERIFIED);
        response.setData(dataMap);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Map<String, Object>>> login(@Valid @RequestBody LoginDtoRequest loginDtoRequest) {
        LoginDtoResponse loginDtoResponse = authService.login(loginDtoRequest);

        dataMap.clear();
        dataMap.put("auth", loginDtoResponse);

        BaseResponse<Map<String, Object>> response = ResponseUtility.getBaseResponse(HttpStatus.OK.value(), LOGIN_SUCCESSFULLY, dataMap);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Re-send OTP")
    @PostMapping("/otp/resend")
    public ResponseEntity<BaseResponse<Map<String, Object>>> resendOtp(@Valid @RequestBody ResendRequestDto resendRequestDto) {
        authService.resendOtp(resendRequestDto);
        BaseResponse<Map<String, Object>> response = ResponseUtility.getBaseResponse(HttpStatus.OK.value(), RESEND_OTP_SUCCESSFULLY, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Change password")
    @PostMapping("/password/change")
    public ResponseEntity<BaseResponseWithoutData> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        // TODO: Check if OTP that verified before was for change password
        authService.changePassword(request);
        BaseResponseWithoutData response = BaseResponseWithoutData.builder()
                .code(HttpStatus.OK.value())
                .message(CHANGE_PASSWORD_SUCCESSFULLY)
                .build();
        return ResponseEntity.ok(response);
    }

}
