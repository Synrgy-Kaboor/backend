package com.synrgy.kaboor.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples")
public class ExampleController {

    @GetMapping
    public String getExample() {
        return "Example";
    }

}
