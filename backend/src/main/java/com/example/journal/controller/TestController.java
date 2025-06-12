package com.example.journal.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TestController {
    
    @GetMapping("/test")
    public String testConnection() {
        return "З'єднання успішне!";
    }
    
     @GetMapping("/api/test")
    public String apiTest() {
        return "API працює!";
    }
}