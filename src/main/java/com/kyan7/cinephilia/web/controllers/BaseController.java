package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.service.UserServiceModel;
import com.kyan7.cinephilia.domain.models.view.UserAuthoritiesViewModel;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.stream.Collectors;

/**
 * Generalizes common methods among all controllers.
 */
public abstract class BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    protected BaseController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Helps load web pages which require server information (e.g. article ids, movie titles, etc.).
     * @param view shows which page to visualize.
     * @param modelAndView is used for transfering data to the pages.
     * @return a view of the page.
     */
    protected ModelAndView view(String view, ModelAndView modelAndView) {
        modelAndView.setViewName(view);
        return modelAndView;
    }

    /**
     * Helps load web pages which do not require server information (e.g. index).
     * @param view shows which page to visualize.
     * @return a view of the page.
     */
    protected ModelAndView view(String view) {
        return this.view(view, new ModelAndView());
    }

    /**
     * Redirects us to a url.
     * @param url
     * @return a view of the page.
     */
    protected ModelAndView redirect(String url) {
        return this.view("redirect:" + url);
    }

    protected UserAuthoritiesViewModel findCurrentUser(Principal principal) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        UserAuthoritiesViewModel currentUser = this.modelMapper.map(userServiceModel, UserAuthoritiesViewModel.class);
        currentUser.setAuthorities(userServiceModel.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));
        return currentUser;
    }
}

