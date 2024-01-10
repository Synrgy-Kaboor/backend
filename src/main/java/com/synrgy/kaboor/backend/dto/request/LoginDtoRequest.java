package com.synrgy.kaboor.backend.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginDtoRequest {

    private String email;

    private String password;

}
