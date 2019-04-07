package com.kyan7.cinephilia.web.controllers;

import org.springframework.web.servlet.ModelAndView;

/**
 * Generalizes common methods among all controllers.
 */
public abstract class BaseController {

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
}

