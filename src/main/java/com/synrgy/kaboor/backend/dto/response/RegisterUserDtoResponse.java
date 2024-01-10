package com.synrgy.kaboor.backend.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterUserDtoResponse {

    private String phoneNumber;

    private String email;

    private String fullName;

    private boolean verified;

}
