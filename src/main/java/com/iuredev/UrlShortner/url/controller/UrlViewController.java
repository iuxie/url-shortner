package com.iuredev.UrlShortner.url.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UrlViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}