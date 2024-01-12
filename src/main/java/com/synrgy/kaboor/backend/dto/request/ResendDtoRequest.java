package com.synrgy.kaboor.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResendDtoRequest {

    @NotNull
    @NotEmpty
    private String email;

}
