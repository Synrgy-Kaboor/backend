package com.synrgy.kaboor.backend.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResendDtoRequest {

    private String email;

}
