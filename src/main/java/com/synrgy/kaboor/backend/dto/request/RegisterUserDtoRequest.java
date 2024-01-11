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
public class RegisterUserDtoRequest {

    @NotEmpty
    @NotNull
    private String phoneNumber;

    @NotEmpty
    @NotNull
    private String email;

    @NotEmpty
    @NotNull
    private String fullName;

    @NotEmpty
    @NotNull
    private String password;

}
