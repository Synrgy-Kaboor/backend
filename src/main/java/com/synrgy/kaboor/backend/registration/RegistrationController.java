package com.synrgy.kaboor.backend.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/auth/register")
@AllArgsConstructor
public class RegistrationController {
    private RegistrationService registrationService;
    @PostMapping()
    public String register(RegistrationRequest request){
        return registrationService.register(request);
    }
}
