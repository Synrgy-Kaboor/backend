package com.synrgy.kaboor.backend.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BaseExceptionResponse {

    private int code;

    private String message;

}
