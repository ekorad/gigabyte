package com.gigabyte.application.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
public class ProtectedResourceController {
    @GetMapping("/resource")
    public Map<String, String> getResource() {
        Map<String, String> map = new HashMap<>();
        map.put("protected resource", "This is some protected resource");
        return map;
    }
}
