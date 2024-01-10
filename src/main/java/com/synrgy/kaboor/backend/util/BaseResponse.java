package com.synrgy.kaboor.backend.util;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {

    private int code;

    private String message;

    private T data;

}
