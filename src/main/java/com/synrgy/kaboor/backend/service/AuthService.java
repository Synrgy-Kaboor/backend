package com.synrgy.kaboor.backend.service;

import com.synrgy.kaboor.backend.dto.request.*;
import com.synrgy.kaboor.backend.dto.response.ForgetPasswordDtoResponse;
import com.synrgy.kaboor.backend.dto.response.LoginDtoResponse;
import com.synrgy.kaboor.backend.dto.response.OtpDtoResponse;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;

public interface AuthService {

    RegisterUserDtoResponse registerAsUser(RegisterUserDtoRequest registerUserDtoRequest);

    OtpDtoResponse verifyOtp(OtpDtoRequest otpDtoRequest);

    LoginDtoResponse login(LoginDtoRequest loginDtoRequest);

    void resendOtp(ResendDtoRequest resendDtoRequest);

    void changePassword(ChangePasswordDtoRequest changePasswordDtoRequest);

    ForgetPasswordDtoResponse forgetPassword(ForgetPasswordDtoRequest forgetPasswordDtoRequest);

    void verifyRequestChangePassword(VerifyRequestChangePasswordDtoRequest request);

}
