package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.MovieTheaterAddBindingModel;
import com.kyan7.cinephilia.domain.models.binding.ScreeningMultiAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.MovieTheaterServiceModel;
import com.kyan7.cinephilia.domain.models.view.MovieTheaterAdminListViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieTheaterBasicViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieTheaterUserListViewModel;
import com.kyan7.cinephilia.service.MovieTheaterService;
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
@RequestMapping("/movie-theaters")
public class MovieTheaterController extends BaseController {

    private final MovieTheaterService movieTheaterService;
    private final ModelMapper modelMapper;

    @Autowired
    protected MovieTheaterController(UserService userService, ModelMapper modelMapper, MovieTheaterService movieTheaterService, ModelMapper modelMapper1) {
        super(userService, modelMapper);
        this.movieTheaterService = movieTheaterService;
        this.modelMapper = modelMapper1;
    }

    /**
     * Loads a view of the user-friendly list of movie theaters.
     * @param modelAndView allows us to attach a list of movie theaters to visualize; also allows us to attach "Movie Theater List" to the title (e.g. "Movie Theater List - Cinephilia").
     * @return a view of the page (if there are no errors) or a redirect to the Home page (if there are).
     */
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView listMovieTheaters(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Movie Theater List");

            List<MovieTheaterUserListViewModel> movieTheaters = this.movieTheaterService.findAllMovieTheatersOrderByName()
                    .stream()
                    .map(t -> this.modelMapper.map(t, MovieTheaterUserListViewModel.class))
                    .collect(Collectors.toList());
            modelAndView.addObject("movieTheaters", movieTheaters);

            return view("movie-theater/list-movie-theaters", modelAndView);
        } catch (Exception e) {
            return redirect("home");
        }
    }

    /**
     * Loads a view of all movie theaters. Only for admins.
     * @param modelAndView allows us to attach a list of movies theaters to visualize; also allows us to attach "All Movie Theaters" to the title of the page (e.g. "All Movie Theaters - Cinephilia").
     * @return a view of the page (if there are no errors) or a redirect to the Home page (if there are).
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allMovieTheaters(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "All Movie Theaters");
        List<MovieTheaterAdminListViewModel> movieTheaters = this.movieTheaterService.findAllMovieTheaters()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterAdminListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("movieTheaters", movieTheaters);

        return view("movie-theater/all-movie-theaters", modelAndView);
    }

    /**
     * Loads a view of the Add Movie Theater page. Only for admins.
     * @param modelAndView allows us to attach "Add Movie Theater" to the page title (e.g. "Add Movie Theater - Cinephilia).
     * @return a view of the page (if there are no errors) or a redirect to the All Movies page (if there are).
     */
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieTheater(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Add Movie Theater");

            return view("movie-theater/add-movie-theater", modelAndView);
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to add an appropriate entity to the database. Only for admins.
     * @param model is the collection of submitted data.
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieTheaterConfirm(@ModelAttribute(name = "model") MovieTheaterAddBindingModel model) {
        try {
            MovieTheaterServiceModel movieTheaterServiceModel = this.modelMapper.map(model, MovieTheaterServiceModel.class);
            this.movieTheaterService.addMovieTheater(movieTheaterServiceModel);

            return redirect("/movie-theaters/all");
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    /**
     * Load a view of a chosen movie theater's Edit page. Only for admins.
     * @param id is the id of the movie theater we're editing.
     * @param modelAndView allows us to attach multiple objects to the page.
     * @return a view of the chosen movie theater's Edit page (if there are no errors) or a redirect to the All Movie Theaters page.
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieTheater(@PathVariable String id, ModelAndView modelAndView) {
        try {
            MovieTheaterServiceModel movieTheaterServiceModel = this.movieTheaterService.findMovieTheaterById(id);
            MovieTheaterAddBindingModel model = this.modelMapper.map(movieTheaterServiceModel, MovieTheaterAddBindingModel.class);
            modelAndView.addObject("pageTitle", "Edit t:" + model.getName());

            modelAndView.addObject("movieTheater", model);

            modelAndView.addObject("movieTheaterId", id);

            return view("movie-theater/edit-movie-theater", modelAndView);
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to edit the respective movie theater in the database. Only for admins.
     * @param id is the id of the movie theater we're editing.
     * @param model is the collection of submitted data.
     * @return a redirect to the All Movie Theaters page.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieTheaterConfirm(@PathVariable String id, @ModelAttribute MovieTheaterAddBindingModel model) {
        try {
            MovieTheaterServiceModel movieTheaterServiceModel = this.modelMapper.map(model, MovieTheaterServiceModel.class);
            this.movieTheaterService.editMovieTheater(id, movieTheaterServiceModel);

            return redirect("/movie-theaters/all");
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    /**
     * Attempts to delete a chosen movie theater. Only for admins.
     * @param id is the id of the movie theater we're deleting.
     * @return a redirect to the All Movie Theaters page.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteMovieTheater(@PathVariable String id) {
        try {
            this.movieTheaterService.deleteMovie(id);

            return redirect("/movie-theaters/all");
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    /**
     * Finds all movie theaters. Used for loading options for screenings when they are being created.
     * @return all movies theaters in the database.
     * @see MovieController#addScreening(String, ScreeningMultiAddBindingModel)
     */
    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<MovieTheaterBasicViewModel> fetchMovies() {
        return this.movieTheaterService.findAllMovieTheaters()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterBasicViewModel.class))
                .collect(Collectors.toList());
    }
}
