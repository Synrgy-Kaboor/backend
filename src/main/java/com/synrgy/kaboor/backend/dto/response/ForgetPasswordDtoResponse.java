package com.synrgy.kaboor.backend.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ForgetPasswordDtoResponse {

    private long nextFiveMinutesOnSeconds;

    private boolean requestForChangePasswordVerified;

}
