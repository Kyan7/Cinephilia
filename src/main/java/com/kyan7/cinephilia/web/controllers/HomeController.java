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

    /**
     * Loads a view of the index page.
     * @param principal is used to find whether there is a currently logged-in user.
     * @param modelAndView is used to attach "Index" to the page title (e.g. "Index - Cinephilia").
     * @return a redirect to the home page (if there is a logged-in user) or a view of the index page (if there isn't)
     */
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

    /**
     * Loads a view of the Home page.
     * @param principal is used to find the current user's username.
     * @param modelAndView allows us to attach the current user's username to the page; allows us to attach "Home" to the page title ("Home - Cinephilia).
     * @return a view of the Home page.
     */
    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Home");

        modelAndView.addObject("currentUser", principal.getName());

        return view("home", modelAndView);
    }
}

