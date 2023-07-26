package com.example.traineejava.controllers;

import com.example.traineejava.models.Cafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/")
    public String Main(Model model)
    {
        return "cafe/cafe-main";
    }
}
