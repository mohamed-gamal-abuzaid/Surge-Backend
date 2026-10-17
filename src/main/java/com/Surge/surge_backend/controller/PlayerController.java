package com.Surge.surge_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayerController {

    @GetMapping("/player")
    public String getPlayer() {
        return "forward:/player.html";
    }
}
