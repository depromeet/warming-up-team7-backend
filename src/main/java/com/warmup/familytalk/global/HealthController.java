package com.warmup.familytalk.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    static final String SUCCESS_MESSAGE = "SUCCESS";

    @GetMapping
    public ResponseEntity health(){
        return ResponseEntity.ok(SUCCESS_MESSAGE);
    }
}