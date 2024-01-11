package com.synrgy.kaboor.backend.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResendRequestDto {

    private String email;

}
