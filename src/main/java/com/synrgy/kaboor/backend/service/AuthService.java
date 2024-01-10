package com.synrgy.kaboor.backend.service;

import com.synrgy.kaboor.backend.dto.request.RegisterUserDtoRequest;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;

public interface AuthService {

    RegisterUserDtoResponse registerAsUser(RegisterUserDtoRequest registerUserDtoRequest);

}
