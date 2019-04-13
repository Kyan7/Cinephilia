package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.entities.Screening;
import com.kyan7.cinephilia.domain.models.binding.MovieAddBindingModel;
import com.kyan7.cinephilia.domain.models.binding.ReviewAddBindingModel;
import com.kyan7.cinephilia.domain.models.binding.ScreeningMultiAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.*;
import com.kyan7.cinephilia.domain.models.view.*;
import com.kyan7.cinephilia.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MovieController extends BaseController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final ReviewService reviewService;
    private final ScreeningService screeningService;
    private final MovieTheaterService movieTheaterService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;


    @Autowired
    public MovieController(MovieService movieService, GenreService genreService, ReviewService reviewService, ScreeningService screeningService, MovieTheaterService movieTheaterService, UserService userService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        super(userService, modelMapper);
        this.movieService = movieService;
        this.genreService = genreService;
        this.reviewService = reviewService;
        this.screeningService = screeningService;
        this.movieTheaterService = movieTheaterService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    /**
     * Loads a view of all movies. Only for admins.
     * @param modelAndView allows us to attach a list of movies to visualize; also allows us to attach "All Movies" to the title of the page (e.g. "All Movies - Cinephilia").
     * @return a view of the page (if there are no errors) or the home page (if there are).
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allMovies(ModelAndView modelAndView) {
        try {
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

            return view("movie/all-movies", modelAndView);
        } catch (Exception e) {
            return redirect("home");
        }
    }

    /**
     * Loads a view of the Add Movie page.
     * @param modelAndView allows us to attach a list of all possible genres to the page; also allows us to attach "Add Movie" to the page title (e.g. "Add Movie - Cinephilia). Only for admins.
     * @return a view of the page (if there are no errors) or the All Movies page (if there are).
     */
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovie(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Add Movie");

            List<GenreViewModel> genres = this.genreService.findAllGenresOrderByName()
                    .stream()
                    .map(g -> this.modelMapper.map(g, GenreViewModel.class))
                    .collect(Collectors.toList());
            modelAndView.addObject("genres", genres);

            return view("movie/add-movie", modelAndView);
        } catch (Exception e) {
            return redirect("/movies/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to add an appropriate entity to the database. Only for admins.
     * @param model is the collection of submitted data.
     * @param principal is used to get the username of the current user for the purposes of storing who created the movie.
     * @return a view of the All Movies page.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieConfirm(@ModelAttribute MovieAddBindingModel model, Principal principal) {
        try {
            MovieServiceModel movieServiceModel = this.modelMapper.map(model, MovieServiceModel.class);
            List<GenreServiceModel> genres = this.genreService.findAllGenresOrderByName()
                    .stream()
                    .filter(g -> model.getGenres().contains(g.getId()))
                    .collect(Collectors.toList());
            movieServiceModel.setGenres(genres);

            String imageUrl = this.cloudinaryService.uploadImage(model.getImage());
            movieServiceModel.setImageUrl(imageUrl);

            movieServiceModel.setUser(this.userService.findUserByUsername(principal.getName()));
            this.movieService.addMovie(movieServiceModel);

            return redirect("/movies/all");
        } catch (Exception e) {
            return redirect("/movies/all");
        }

    }

    /**
     * Loads a view of a chosen movie's Details page. This includes all reviews and screenings (which are grouped up accordingly).
     * @param id is the id of the movie which is being viewed.
     * @param principal is used to get the username of the current user for the purposes of finding their authorities and storing information who wrote a review.
     * @param modelAndView allows us to attach multiple objects to the page.
     * @return a view of the chosen movie's Details page (if there are no errors) or a redirect to either the All Movies page (for admins) or the Movies List page (for normal users).
     * @see #findTrailerIds(MovieServiceModel)
     * @see #groupScreenings(List)
     * @see #findAverageUserRating(List)
     */
    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsMovie(@PathVariable String id, Principal principal, ModelAndView modelAndView) {
        UserAuthoritiesViewModel currentUser = this.findCurrentUser(principal);
        try {
            modelAndView.addObject("currentUser", currentUser);

            MovieServiceModel movieServiceModel = this.movieService.findMovieByIdAndIncrementViews(id);
            modelAndView.addObject("pageTitle", movieServiceModel.getTitle());

            MovieDetailsViewModel movie = this.modelMapper.map(movieServiceModel, MovieDetailsViewModel.class);
            movie.setUser(movieServiceModel.getUser().getUsername());
            List<String> genres = movieServiceModel.getGenres()
                    .stream()
                    .map(g -> g.getName())
                    .distinct()
                    .collect(Collectors.toList());
            movie.setGenres(genres);
            modelAndView.addObject("movie", movie);

            modelAndView.addObject("trailerIds", findTrailerIds(movieServiceModel));

            List<ScreeningViewModel> screenings = this.screeningService.findAllScreeningsByMovieId(id)
                    .stream()
                    .map(s -> {
                        ScreeningViewModel screeningViewModel = this.modelMapper.map(s, ScreeningViewModel.class);
                        screeningViewModel.setMovieTheater(s.getMovieTheater().getName());
                        return screeningViewModel;
                    })
                    .collect(Collectors.toList());
            modelAndView.addObject("groupedScreenings", groupScreenings(screenings));


            List<ReviewViewModel> reviews = this.reviewService.findAllReviewsByMovieId(id)
                    .stream()
                    .map(r -> {
                        ReviewViewModel reviewViewModel = this.modelMapper.map(r, ReviewViewModel.class);
                        reviewViewModel.setReviewer(r.getReviewer().getUsername());
                        return reviewViewModel;
                    })
                    .collect(Collectors.toList());
            modelAndView.addObject("reviews", reviews);

            modelAndView.addObject("averageUserRating", findAverageUserRating(reviews));

            return view("movie/details-movie", modelAndView);
        } catch (Exception e) {
            if (currentUser.getAuthorities().contains("ROLE_ADMIN")) {
                return redirect("/movies/all");
            } else {
                return redirect("/movies/list");
            }
        }
    }

    /**
     * Load a view of a chosen movie's Edit page. Only for admins.
     * @param id is the id of the movie we're editing.
     * @param modelAndView allows us to attach multiple objects to the page.
     * @return a view of the chosen movie's Edit page (if there are no errors) or redirects us to the All Movies page.
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovie(@PathVariable String id, ModelAndView modelAndView) {
        try {
            modelAndView.addObject("movieId", id);

            MovieServiceModel movieServiceModel = this.movieService.findMovieById(id);
            MovieAddBindingModel model = this.modelMapper.map(movieServiceModel, MovieAddBindingModel.class);
            modelAndView.addObject("pageTitle", "Edit m:" + model.getTitle());

            model.setGenres(movieServiceModel.getGenres().stream().map(g -> g.getName()).collect(Collectors.toList()));
            modelAndView.addObject("movie", model);

            return view("movie/edit-movie", modelAndView);
        } catch (Exception e) {
            return redirect("/movies/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to edit the respective movie in the database. If no genres were selected in the form, genres do not get edited. Only for admins.
     * @param id is the id of the movie we're editing.
     * @param model is the collection of submitted data.
     * @return a redirect to the movie's Details page.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieConfirm(@PathVariable String id, @ModelAttribute MovieAddBindingModel model) {
        try {
            MovieServiceModel movieServiceModel = this.modelMapper.map(model, MovieServiceModel.class);
            try {
                List<GenreServiceModel> genreServiceModels = new ArrayList<>();
                for (String genreId : model.getGenres()) {
                    genreServiceModels.add(this.modelMapper.map(this.genreService.findGenreById(genreId), GenreServiceModel.class));
                }
                movieServiceModel.setGenres(genreServiceModels);
                this.movieService.editMovieWithEditedGenres(id, movieServiceModel);

                return redirect("/movies/details/" + id);
            } catch (Exception e) {
                this.movieService.editMovieWithUneditedGenres(id, movieServiceModel);

                return redirect("/movies/details/" + id);
            }
        } catch (Exception e) {
            return redirect("/movies/details/" + id);
        }
    }

    /**
     * Attempts to delete a chosen movie. Only for admins.
     * @param id is the id of the movie we're deleting.
     * @return a redirect to the All Movies page.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteMovie(@PathVariable String id) {
        try {
            this.movieService.deleteMovie(id);

            return redirect("/movies/all");
        } catch (Exception e) {
            return redirect("/movies/all");
        }
    }

    /**
     * Submits the gathered data (from the review form on the web page) and attempts to add a review to the movie.
     * @param movieId is the id of the movie we are reviewing.
     * @param principal is used to get the username of the current user for the purposes of storing information who wrote a review.
     * @param model is the collection of submitted data.
     * @return a redirect to the movie's Details page.
     */
    @PostMapping("/add-review/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addReview(@PathVariable String movieId, Principal principal, @ModelAttribute(name = "model") ReviewAddBindingModel model) {
        try {
            ReviewServiceModel reviewServiceModel = this.modelMapper.map(model, ReviewServiceModel.class);
            reviewServiceModel.setReviewer(this.userService.findUserByUsername(principal.getName()));
            reviewServiceModel.setMovie(this.movieService.findMovieById(movieId));
            this.reviewService.addReview(reviewServiceModel);

            return redirect("/movies/details/" + movieId);
        } catch (Exception e) {
            return redirect("/movies/details/" + movieId);
        }
    }

    /**
     * Attempts to delete a review. Only the person who posted the respective review or an admin can remove the review.
     * @param movieId is the id of the movie from which we are removing a review.
     * @param reviewId is the id of the review we are removing.
     * @param principal is used to get the username of the current user for the purposes of finding their authorities and checking whether it matches that of the reviewer.
     * @return a redirect to the movie's Details page.
     */
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

            return redirect("/movies/details/" + movieId);
        } catch (Exception e) {
            return redirect("/movies/details/" + movieId);
        }
    }

    /**
     * Submits the gathered data (from the review form on the web page) and attempts to add multiple screenings to the movie. Only for admins
     * @param movieId is the id of the movie to which we are adding screenings.
     * @param multiModel is the collection of submitted data; possibly contains multiple screening times which are split into different screenings.
     * @return a redirect to the movie's Details page.
     */
    @PostMapping("/add-screening/{movieId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addScreening(@PathVariable String movieId, @ModelAttribute(name = "multiModel") ScreeningMultiAddBindingModel multiModel) {
        try {
            MovieTheaterServiceModel movieTheater = this.movieTheaterService.findMovieTheaterById(multiModel.getMovieTheater());
            MovieServiceModel movie = this.movieService.findMovieById(movieId);
            Arrays.asList(multiModel.getTimeStamps().split(", "))
                    .stream()
                    .forEach(ts -> {
                        try {
                            ScreeningServiceModel current = this.modelMapper.map(multiModel, ScreeningServiceModel.class);
                            current.setTimeStamp(ts);
                            current.setMovieTheater(movieTheater);
                            current.setMovie(movie);
                            this.screeningService.addScreening(current);
                        } catch (Exception e) {
                        }
                    });

            return redirect("/movies/details/" + movieId);
        } catch (Exception e) {
            return redirect("/movies/details/" + movieId);
        }
    }

    /**
     * Attempts to delete a screening. Only for admins.
     * @param movieId is the id of the movie from which we are removing the screening.
     * @param screeningId is the id of the screening which we are removing.
     * @return a redirect to the movie's Details page.
     */
    @PostMapping("/delete-screening/{movieId}/{screeningId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteScreening(@PathVariable String movieId, @PathVariable String screeningId) {
        try {
            this.screeningService.deleteScreening(screeningId);

            return redirect("/movies/details/" + movieId);
        } catch (Exception e) {
            return redirect("/movies/details/" + movieId);
        }
    }

    /**
     * Finds all movies (ordered by title). Used for loading options for the Associated Movies section in when adding or editing an article.
     * @return all movies in the database (ordered by title).
     * @see ArticleController#addArticle(ModelAndView)
     * @see ArticleController#editArticle(String, ModelAndView)
     */
    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<MovieBasicViewModel> fetchMovies() {
        return this.movieService.findAllMoviesOrderByTitle()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieBasicViewModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Extracts trailer id's from trailer links. Used for embedding videos.
     * @param movieServiceModel is the movie from which we are getting the trailer links.
     * @return the extracted trailer ids.
     */
    private List<String> findTrailerIds(MovieServiceModel movieServiceModel) {
        List<String> trailerLinks = Arrays.asList(movieServiceModel
                .getTrailerLinks()
                .split(", "));
        List<String> trailerIds = new ArrayList<>();
        for (String trailerLink : trailerLinks) {
            trailerIds.add(trailerLink.split("=")[1]);
        }
        return trailerIds;
    }

    /**
     * Groups up all screenings base on their movie theater, type and price.
     * @param screenings a list of all screenings of a movie.
     * @return all screenings (grouped up accordingly).
     */
    private HashMap<String, HashMap<String, HashMap<Double, ArrayList<ScreeningViewModel>>>> groupScreenings(List<ScreeningViewModel> screenings) {
        HashMap<String, HashMap<String, HashMap<Double, ArrayList<ScreeningViewModel>>>> groupedScreenings = new LinkedHashMap<>();
        for (ScreeningViewModel screening : screenings) {
            if (!groupedScreenings.containsKey(screening.getMovieTheater())) {
                groupedScreenings.put(screening.getMovieTheater(), new HashMap<>());
            }
            if (!groupedScreenings.get(screening.getMovieTheater()).containsKey(screening.getType())) {
                groupedScreenings.get(screening.getMovieTheater()).put(screening.getType(), new HashMap<>());
            }
            if (!groupedScreenings.get(screening.getMovieTheater()).get(screening.getType()).containsKey(screening.getPrice())) {
                groupedScreenings.get(screening.getMovieTheater()).get(screening.getType()).put(screening.getPrice(), new ArrayList<>());
            }
            groupedScreenings.get(screening.getMovieTheater()).get(screening.getType()).get(screening.getPrice()).add(screening);
        }
        return groupedScreenings;
    }

    /**
     * Finds the average user rating among all reviews.
     * @param reviews is the list of reviews belonging to a movie
     * @return the average user rating if there are user reviews, otherwise "N/A"
     */
    private String findAverageUserRating(List<ReviewViewModel> reviews) {
        if (reviews.size() != 0) {
            String averageUserRating;
            Double sum = 0.0;
            long count = 0;
            for (ReviewViewModel review : reviews) {
                sum += review.getRating();
                count++;
            }
            averageUserRating = String.format("%.2f", sum / count).replace(',', '.');

            return averageUserRating;
        }
        return "N/A";
    }
}
