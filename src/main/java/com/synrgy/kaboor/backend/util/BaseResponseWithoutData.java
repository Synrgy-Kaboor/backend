package com.synrgy.kaboor.backend.util;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BaseResponseWithoutData {

    private int code;

    private String message;

}
