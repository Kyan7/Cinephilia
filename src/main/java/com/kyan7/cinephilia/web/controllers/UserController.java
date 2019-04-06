package com.kyan7.cinephilia.web.controllers;


import com.kyan7.cinephilia.domain.models.binding.UserRegisterBindingModel;
import com.kyan7.cinephilia.domain.models.service.UserServiceModel;
import com.kyan7.cinephilia.domain.models.view.UserProfileViewModel;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register() {
        return super.view("register");
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute(name = "model") UserRegisterBindingModel model, ModelAndView modelAndView) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            return super.view("register");
        }
        this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class));
        return super.redirect("/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(ModelAndView modelAndView, HttpSession session) {
        return super.view("login");
    }

    //@GetMapping("/logout")
    //public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
    //    if (session.getAttribute("username") == null) {
    //        modelAndView.setViewName("redirect:/login");
    //    } else {
    //        session.invalidate();
    //        modelAndView.setViewName("redirect:/");
    //    }
    //    return modelAndView;
    //}

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {
        modelAndView
                .addObject("model", this.modelMapper.map(this.userService.findUserByUsername(principal.getName()), UserProfileViewModel.class));
        return super.view("profile", modelAndView);
    }
}
