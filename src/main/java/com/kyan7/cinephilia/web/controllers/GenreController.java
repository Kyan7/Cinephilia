package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.GenreAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.GenreServiceModel;
import com.kyan7.cinephilia.domain.models.view.GenreViewModel;
import com.kyan7.cinephilia.service.GenreService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/genres")
public class GenreController extends BaseController{

    private final UserService userService;
    private final GenreService genreService;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreController(UserService userService, GenreService genreService, ModelMapper modelMapper) {
        super(userService, modelMapper);
        this.userService = userService;
        this.genreService = genreService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allGenres(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "All Genres");
        List<GenreViewModel> genres = this.genreService.findAllGenresOrderByName()
                .stream()
                .map(g -> this.modelMapper.map(g, GenreViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("genres", genres);
        return super.view("genre/all-genres", modelAndView);
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addGenre(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Add Genre");
        return super.view("genre/add-genre", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addGenreConfirm(@ModelAttribute(name = "model") GenreAddBindingModel model) {
        try {
            this.genreService.addGenre(this.modelMapper.map(model, GenreServiceModel.class));
            return super.redirect("all");
        } catch (IllegalArgumentException iae) {
            return super.redirect("all");
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editGenre(@PathVariable String id, ModelAndView modelAndView) {
        GenreViewModel genreViewModel = this.modelMapper.map(this.genreService.findGenreById(id), GenreViewModel.class);
        modelAndView.addObject("pageTitle", "Edit g:" + genreViewModel.getName());
        modelAndView.addObject("model", genreViewModel);
        return super.view("genre/edit-genre", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editGenreConfirm(@PathVariable String id, @ModelAttribute GenreAddBindingModel model) {
        try {
            this.genreService.editGenre(id, this.modelMapper.map(model, GenreServiceModel.class));
            return super.redirect("/genres/all");
        } catch (Exception e) {
            return super.redirect("/genres/all");
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteGenre(@PathVariable String id) {
        try {
            this.genreService.deleteGenre(id);
            return super.redirect("/genres/all");
        } catch (Exception e) {
            return super.redirect("/genres/all");
        }
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<GenreViewModel> fetchGenres() {
        return this.genreService.findAllGenresOrderByName()
                .stream()
                .map(g -> this.modelMapper.map(g, GenreViewModel.class))
                .collect(Collectors.toList());
    }
}
