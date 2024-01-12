package com.synrgy.kaboor.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginDtoRequest {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

}
