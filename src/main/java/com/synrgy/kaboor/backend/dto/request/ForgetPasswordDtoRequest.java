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
public class ForgetPasswordDtoRequest {

    @NotNull(message = "Email cannot be null!")
    @NotEmpty(message = "Email cannot be empty!")
    private String email;

}
