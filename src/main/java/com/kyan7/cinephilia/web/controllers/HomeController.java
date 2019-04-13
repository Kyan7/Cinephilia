package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController extends BaseController {

    @Autowired
    public HomeController(UserService userService, ModelMapper modelMapper) {
        super(userService, modelMapper);
    }

    @GetMapping("/")
    public ModelAndView index(Principal principal, ModelAndView modelAndView) {
        try {
            principal.getName();

            return redirect("/home");
        } catch (Exception e) {
            modelAndView.addObject("pageTitle", "Index");
            return view("index", modelAndView);
        }
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Home");

        modelAndView.addObject("currentUser", principal.getName());

        return view("home", modelAndView);
    }
}

