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

    private final GenreService genreService;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreController(UserService userService, GenreService genreService, ModelMapper modelMapper) {
        super(userService, modelMapper);
        this.genreService = genreService;
        this.modelMapper = modelMapper;
    }

    /**
     * Loads a view of all genres. Only for admins.
     * @param modelAndView allows us to attach a list of genres to visualize; also allows us to attach "All Genres" to the title of the page (e.g. "All Genres - Cinephilia").
     * @return a view of the page (if there are no errors) or a redirect to the Home page (if there are).
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allGenres(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "All Genres");
            List<GenreViewModel> genres = this.genreService.findAllGenresOrderByName()
                    .stream()
                    .map(g -> this.modelMapper.map(g, GenreViewModel.class))
                    .collect(Collectors.toList());
            modelAndView.addObject("genres", genres);

            return view("genre/all-genres", modelAndView);
        } catch (Exception e) {
            return redirect("home");
        }
    }

    /**
     * Loads a view of the Add Genre page. Only for admins.
     * @param modelAndView allows us to attach "Add Genre" to the page title (e.g. "Add Genre - Cinephilia).
     * @return a view of the page (if there are no errors) or a redirect to the All Genres page (if there are).
     */
    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addGenre(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Add Genre");

            return view("genre/add-genre", modelAndView);
        } catch (Exception e) {
            return redirect("all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to add an appropriate entity to the database. Only for admins.
     * @param model is the collection of submitted data.
     * @return a view of the All Genres page.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addGenreConfirm(@ModelAttribute(name = "model") GenreAddBindingModel model) {
        try {
            this.genreService.addGenre(this.modelMapper.map(model, GenreServiceModel.class));

            return redirect("all");
        } catch (IllegalArgumentException iae) {
            return redirect("all");
        }
    }

    /**
     * Loads a view of the the chosen genre's Edit page. Only for admins.
     * @param modelAndView allows us to attach "Edit g:" to the page title (e.g. "Edit g:Action - Cinephilia).
     * @return a view of the page (if there are no errors) or a redirect to the All Genres page (if there are).
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editGenre(@PathVariable String id, ModelAndView modelAndView) {
        try {
            GenreViewModel genreViewModel = this.modelMapper.map(this.genreService.findGenreById(id), GenreViewModel.class);
            modelAndView.addObject("pageTitle", "Edit g:" + genreViewModel.getName());

            modelAndView.addObject("model", genreViewModel);

            return view("genre/edit-genre", modelAndView);
        } catch (Exception e) {
            return redirect("/genres/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to edit the respective genre in the database. Only for admins.
     * @param id is the id of the genre we're editing.
     * @param model is the collection of submitted data.
     * @return a redirect to the All Genres page.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editGenreConfirm(@PathVariable String id, @ModelAttribute GenreAddBindingModel model) {
        try {
            this.genreService.editGenre(id, this.modelMapper.map(model, GenreServiceModel.class));

            return redirect("/genres/all");
        } catch (Exception e) {
            return redirect("/genres/all");
        }
    }

    /**
     * Attempts to delete a chosen genre. Only for admins.
     * @param id is the id of the genre we're deleting.
     * @return a redirect to the All Genres page.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteGenre(@PathVariable String id) {
        try {
            this.genreService.deleteGenre(id);
            return redirect("/genres/all");
        } catch (Exception e) {
            return redirect("/genres/all");
        }
    }

    /**
     * Finds all genres (ordered by name). Used for loading options for the Genres section when adding or editing a movie.
     * @return all genres in the database.
     * @see MovieController#addMovie(ModelAndView)
     * @see MovieController#editMovie(String, ModelAndView)
     */
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
