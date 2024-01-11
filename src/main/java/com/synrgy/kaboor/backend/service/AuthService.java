package com.synrgy.kaboor.backend.service;

import com.synrgy.kaboor.backend.dto.request.LoginDtoRequest;
import com.synrgy.kaboor.backend.dto.request.OtpDtoRequest;
import com.synrgy.kaboor.backend.dto.request.RegisterUserDtoRequest;
import com.synrgy.kaboor.backend.dto.request.ResendRequestDto;
import com.synrgy.kaboor.backend.dto.response.LoginDtoResponse;
import com.synrgy.kaboor.backend.dto.response.OtpDtoResponse;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;

public interface AuthService {

    RegisterUserDtoResponse registerAsUser(RegisterUserDtoRequest registerUserDtoRequest);

    OtpDtoResponse verifyOtp(OtpDtoRequest otpDtoRequest);

    LoginDtoResponse login(LoginDtoRequest loginDtoRequest);

    void resendOtp(ResendRequestDto resendRequestDto);

}
