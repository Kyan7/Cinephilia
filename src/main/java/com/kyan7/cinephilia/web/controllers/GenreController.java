package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.GenreAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.GenreServiceModel;
import com.kyan7.cinephilia.domain.models.view.GenreListViewModel;
import com.kyan7.cinephilia.service.GenreService;
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

    private final GenreService genreService;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreController(GenreService genreService, ModelMapper modelMapper) {
        this.genreService = genreService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allGenresAdmin(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "All Genres");
        List<GenreListViewModel> genres = this.genreService.findAllGenresOrderByName()
                .stream()
                .map(g -> this.modelMapper.map(g, GenreListViewModel.class))
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
    public ModelAndView addGenreConfirm(@ModelAttribute(name = "model") GenreAddBindingModel model, ModelAndView modelAndView) {
        this.genreService.addGenre(this.modelMapper.map(model, GenreServiceModel.class));
        return super.redirect("/genres/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editGenre(@PathVariable String id, ModelAndView modelAndView) {
        return null; //modelAndView.addObject("pageTitle", )
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView modelAndView(@PathVariable String id, ModelAndView modelAndView) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteGenre(@PathVariable String id) {
        this.genreService.deleteGenre(id);
        return super.redirect("/genres/all");
    }
}
