package com.example.traineejava.controllers;

import com.example.traineejava.models.*;
import com.example.traineejava.repo.*;
import com.example.traineejava.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;


    @GetMapping("/events")
    public String eventMain(Model model)
    {
        Iterable<Event> events = eventRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", events);//передаем в шаблон

        return "event/event-main";
    }

    @GetMapping("/details")
    public String eventDetails(Model model)
    {
        Iterable<Event> events = eventRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", events);//передаем в шаблон

        return "event/event-details";
    }





}
