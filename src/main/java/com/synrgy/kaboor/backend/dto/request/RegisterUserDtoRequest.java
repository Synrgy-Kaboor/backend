package com.synrgy.kaboor.backend.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterUserDtoRequest {

    private String phoneNumber;

    private String email;

    private String fullName;

    private String password;

}
