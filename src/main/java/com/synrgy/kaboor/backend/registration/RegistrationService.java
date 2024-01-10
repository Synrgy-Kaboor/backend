package com.synrgy.kaboor.backend.registration;

import com.synrgy.kaboor.backend.AppUser.AppUser;
import com.synrgy.kaboor.backend.AppUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    public String register(RegistrationRequest request){
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }


//        return appUserService.signUpUser(
//                new AppUser(
//                        request.g
//                )
//        );
//    }
}
