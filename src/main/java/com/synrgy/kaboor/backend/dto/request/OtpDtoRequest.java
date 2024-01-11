package com.synrgy.kaboor.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OtpDtoRequest {

    @NotEmpty
    @NotNull
    private String otp;

}
