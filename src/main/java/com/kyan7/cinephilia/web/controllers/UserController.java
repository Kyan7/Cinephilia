package com.kyan7.cinephilia.web.controllers;


import com.kyan7.cinephilia.domain.models.binding.UserEditBindingModel;
import com.kyan7.cinephilia.domain.models.binding.UserRegisterBindingModel;
import com.kyan7.cinephilia.domain.models.service.UserServiceModel;
import com.kyan7.cinephilia.domain.models.view.UserProfileViewModel;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Loads a view of the Register page.
     * @param modelAndView allows us to attach "Register" to the title (e.g. "Register - Cinephilia").
     * @return view of the Register page.
     */
    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Register");
        return super.view("register", modelAndView);
    }

    /**
     * Checks whether 'password' and 'confirmPassword' match in the form. If so, attempts to register the user into the database. Redirects to login.
     * @param model contains all the data that was submitted in the registration form. Not needed as of now, but consistency is important.
     * @param modelAndView allows us to attach "Register" to the title (e.g. "Register - Cinephilia").
     * @return view of the Register page if passwords don't match, otherwise view of the Login page
     */
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute(name = "model") UserRegisterBindingModel model, ModelAndView modelAndView) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            modelAndView.addObject("pageTitle", "Register");
            return super.view("register", modelAndView);
        }
        this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class));
        return super.redirect("login");
    }

    /**
     * Loads a view of the Login page.
     * @param modelAndView allows us to attach "Login" to the title (e.g. "Login - Cinephilia").
     * @return view of the Login page.
     */
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Login");
        return super.view("login", modelAndView);
    }

    /**
     * Finds the current user's data by their username (using the Principle class). Loads a view of the current user's Profile page.
     * @param modelAndView allows us to attach "#[currentUser]" to the title (e.g. "#Kyan7 - Cinephilia").
     * @return view of the current user's Profile page.
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {
        UserProfileViewModel userProfileViewModel = this.modelMapper
                .map(this.userService.findUserByUsername(principal.getName()), UserProfileViewModel.class);
        modelAndView.addObject("model", userProfileViewModel);
        modelAndView.addObject("pageTitle", "#" + userProfileViewModel.getUsername());
        return super.view("profile", modelAndView);
    }

    /**
     * Finds the current user's data by their username (using the Principle class). Loads a view of the current user's Edit Profile page (which allows certain changes to their data).
     * @param modelAndView allows us to attach "Edit #[currentUser]" to the title (e.g. "Edit #Kyan7 - Cinephilia").
     * @return view of the current user's Edit Profile page.
     */
    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView) {
        UserProfileViewModel userProfileViewModel = this.modelMapper
                .map(this.userService.findUserByUsername(principal.getName()), UserProfileViewModel.class);
        modelAndView.addObject("model", userProfileViewModel);
        modelAndView.addObject("pageTitle", "Edit #" + userProfileViewModel.getUsername());
        return super.view("edit-profile", modelAndView);
    }

    /**
     * Checks whether 'password' and 'confirmPassword' match in the form. If so, attempts to edit the current user's data. Redirects to the current user's profile.
     * @param model contains all the data that was submitted in the edit profile form.
     * @param modelAndView allows us to attach "Edit #[currentUser]" to the title (e.g. "Edit #Kyan7 - Cinephilia").
     * @return view of the current user's Edit Profile page if passwords don't match, otherwise view of the current user's Profile page
     */
    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute(name = "model") UserEditBindingModel model, ModelAndView modelAndView) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            modelAndView.addObject("pageTitle", "Edit #" + model.getUsername());
            return super.view("edit-profile", modelAndView);
        }
        this.userService.editUserProfile(this.modelMapper.map(model, UserServiceModel.class), model.getOldPassword());
        return super.redirect("/users/profile");
    }
}
