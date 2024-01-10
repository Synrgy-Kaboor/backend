package com.synrgy.kaboor.backend.controller;

import com.synrgy.kaboor.backend.dto.request.RegisterUserDtoRequest;
import com.synrgy.kaboor.backend.dto.response.RegisterUserDtoResponse;
import com.synrgy.kaboor.backend.service.AuthService;
import com.synrgy.kaboor.backend.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.synrgy.kaboor.backend.util.MessageResponse.*;

@Tag(name = "Authentication Resource")
@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final Map<String, Object> dataMap = new HashMap<>();

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

}
