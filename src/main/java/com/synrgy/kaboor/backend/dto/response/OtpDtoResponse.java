package com.synrgy.kaboor.backend.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OtpDtoResponse {

    private String phoneNumber;

    private String email;

    private String fullName;

    private boolean verified;

}
