package com.synrgy.kaboor.backend.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VerifyRequestChangePasswordDtoRequest {

    private String otp;

}
