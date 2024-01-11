package com.synrgy.kaboor.backend.dto.response;

import com.synrgy.kaboor.backend.model.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginDtoResponse {

    private String jwt;

    private Role role;

}
