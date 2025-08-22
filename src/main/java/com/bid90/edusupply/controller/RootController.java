package com.bid90.edusupply.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestController
public class RootController {

    @GetMapping("/")
    Map<String, String>  test(){
        Map<String, String> keyValue = new HashMap<>();
        keyValue.put("LocalDateTime",LocalDateTime.now().toString());
        keyValue.put("Api Doc", "/swagger-ui/index.html");
        return keyValue;
    }
}
