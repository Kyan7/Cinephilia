package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.MovieAddBindingModel;
import com.kyan7.cinephilia.domain.models.binding.ReviewAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.GenreServiceModel;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.domain.models.service.ReviewServiceModel;
import com.kyan7.cinephilia.domain.models.service.UserServiceModel;
import com.kyan7.cinephilia.domain.models.view.*;
import com.kyan7.cinephilia.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MovieController extends BaseController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;


    @Autowired
    public MovieController(MovieService movieService, GenreService genreService, ReviewService reviewService, UserService userService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        super(userService, modelMapper);
        this.movieService = movieService;
        this.genreService = genreService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allMovies(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "All Movies");
        List<MovieAdminListViewModel> movies = this.movieService.findAllMovies()
                .stream()
                .map(m -> {
                    MovieAdminListViewModel movie = this.modelMapper.map(m, MovieAdminListViewModel.class);
                    movie.setGenres(m.getGenres()
                            .stream()
                            .map(g -> g.getName())
                            .collect(Collectors.toList()));
                    movie.setUser(m.getUser().getUsername());
                    return movie;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("movies", movies);
        return super.view("movie/all-movies", modelAndView);
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovie(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Add Movie");
        modelAndView.addObject("genres",
                this.genreService.findAllGenresOrderByName()
                        .stream()
                        .map(g -> this.modelMapper.map(g, GenreViewModel.class))
                        .collect(Collectors.toList()));
        return super.view("movie/add-movie", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieConfirm(@ModelAttribute(name = "model") MovieAddBindingModel model, Principal principal, ModelAndView modelAndView) throws IOException {
        try {
            MovieServiceModel movieServiceModel = this.modelMapper.map(model, MovieServiceModel.class);
            movieServiceModel.setGenres(
                    this.genreService.findAllGenresOrderByName()
                            .stream()
                            .filter(g -> model.getGenres().contains(g.getId()))
                            .collect(Collectors.toList())
            );
            movieServiceModel.setImageUrl(
                    this.cloudinaryService.uploadImage(model.getImage())
            );
            movieServiceModel.setUser(this.userService.findUserByUsername(principal.getName()));
            this.movieService.addMovie(movieServiceModel);
            return super.redirect("/movies/all");
        } catch (Exception e) {
            return super.redirect("/movies/all");
        }

    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsMovie(@PathVariable String id, Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("currentUser", findCurrentUser(principal));

        MovieServiceModel movieServiceModel = this.movieService.findMovieByIdAndIncrementViews(id);
        modelAndView.addObject("pageTitle", movieServiceModel.getTitle());

        MovieDetailsViewModel movie = this.modelMapper.map(movieServiceModel, MovieDetailsViewModel.class);
        movie.setUser(movieServiceModel.getUser().getUsername());
        movie.setGenres(movieServiceModel.getGenres()
                .stream()
                .map(g -> g.getName())
                .distinct()
                .collect(Collectors.toList()));
        modelAndView.addObject("movie", movie);

        List<String> trailerLinks = Arrays.asList(movieServiceModel
                .getTrailerLinks()
                .split(", "));
        List<String> trailerIds = new ArrayList<>();
        for (String trailerLink : trailerLinks
             ) {
            trailerIds.add(trailerLink.split("=")[1]);
        }
        modelAndView.addObject("trailerIds", trailerIds);

        List<ReviewViewModel> reviews = this.reviewService.findAllReviewsByMovieId(id)
                .stream()
                .map(r -> {
                    ReviewViewModel reviewViewModel = this.modelMapper.map(r, ReviewViewModel.class);
                    reviewViewModel.setReviewer(r.getReviewer().getUsername());
                    return reviewViewModel;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("reviews", reviews);

        String averageUserRating = "N/A";
        Double sum = 0.0;
        long count = 0;
        for (ReviewViewModel review : reviews
             ) {
            sum += review.getRating();
            count++;
        }
        if (count > 0) averageUserRating = String.format("%.2f", sum/count).replace(',', '.');
        modelAndView.addObject("averageUserRating", averageUserRating);

        return super.view("movie/details-movie", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovie(@PathVariable String id, ModelAndView modelAndView) {
        MovieServiceModel movieServiceModel = this.movieService.findMovieById(id);
        MovieAddBindingModel model = this.modelMapper.map(movieServiceModel, MovieAddBindingModel.class);
        model.setGenres(movieServiceModel.getGenres().stream().map(g -> g.getName()).collect(Collectors.toList()));
        modelAndView.addObject("pageTitle", "Edit m:" + model.getTitle());
        modelAndView.addObject("movie", model);
        modelAndView.addObject("movieId", id);

        return super.view("movie/edit-movie", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieConfirm(@PathVariable String id, @ModelAttribute MovieAddBindingModel model) {
        try {
            MovieServiceModel movieServiceModel = this.modelMapper.map(model, MovieServiceModel.class);
            try {
                List<GenreServiceModel> genreServiceModels = new ArrayList<>();
                for (String genreId : model.getGenres()
                ) {
                    genreServiceModels.add(this.modelMapper.map(this.genreService.findGenreById(genreId), GenreServiceModel.class));
                }
                movieServiceModel.setGenres(genreServiceModels);
                this.movieService.editMovieWithEditedGenres(id, movieServiceModel);
                return super.redirect("/movies/details/" + id);
            } catch (Exception e) {
                this.movieService.editMovieWithUneditedGenres(id, movieServiceModel);
                return super.redirect("/movies/details/" + id);
            }
        } catch (Exception e) {
            return super.redirect("/movies/details/" + id);
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteMovie(@PathVariable String id) {
        try {
            this.movieService.deleteMovie(id);
            return super.redirect("/movies/all");
        } catch (Exception e) {
            return super.redirect("/movies/all");
        }
    }

    @PostMapping("/add-review/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addReview(@PathVariable String movieId, Principal principal, @ModelAttribute(name = "model") ReviewAddBindingModel model) {
        try {
            ReviewServiceModel reviewServiceModel = this.modelMapper.map(model, ReviewServiceModel.class);
            reviewServiceModel.setReviewer(this.userService.findUserByUsername(principal.getName()));
            reviewServiceModel.setMovie(this.movieService.findMovieById(movieId));
            this.reviewService.addReview(reviewServiceModel);
            return super.redirect("/movies/details/" + movieId);
        } catch (Exception e) {
            return super.redirect("/movies/details/" + movieId);
        }
    }

    @PostMapping("/delete-review/{movieId}/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView deleteReview(@PathVariable String movieId, @PathVariable String reviewId, Principal principal) {
        try {
            ReviewServiceModel reviewServiceModel = this.reviewService.findReviewById(reviewId);
            UserAuthoritiesViewModel currentUser = findCurrentUser(principal);
            if (currentUser.getAuthorities().contains("ROLE_ADMIN")
                    || reviewServiceModel.getReviewer().getUsername().equals(principal.getName())) {
                this.reviewService.deleteReview(reviewId);
            }
            return super.redirect("/movies/details/" + movieId);
        } catch (Exception e) {
            return super.redirect("/movies/details/" + movieId);
        }
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<MovieBasicViewModel> fetchMovies() {
        return this.movieService.findAllMoviesOrderByTitle()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieBasicViewModel.class))
                .collect(Collectors.toList());
    }
}
