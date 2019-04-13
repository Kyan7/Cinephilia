package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.ArticleAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.ArticleServiceModel;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.domain.models.view.ArticleAdminListViewModel;
import com.kyan7.cinephilia.domain.models.view.ArticleDetailsViewModel;
import com.kyan7.cinephilia.domain.models.view.ArticleUserListViewModel;
import com.kyan7.cinephilia.domain.models.view.UserAuthoritiesViewModel;
import com.kyan7.cinephilia.service.ArticleService;
import com.kyan7.cinephilia.service.CloudinaryService;
import com.kyan7.cinephilia.service.MovieService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/articles")
public class ArticleController extends BaseController {

    private final ArticleService articleService;
    private final MovieService movieService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    protected ArticleController(UserService userService, ModelMapper modelMapper, ArticleService articleService, MovieService movieService, UserService userService1, CloudinaryService cloudinaryService, ModelMapper modelMapper1) {
        super(userService, modelMapper);
        this.articleService = articleService;
        this.movieService = movieService;
        this.userService = userService1;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper1;
    }

    /**
     * Loads a view of the user-friendly list of articles.
     * @param modelAndView allows us to attach a list of articles to visualize; also allows us to attach "Article List" to the title (e.g. "Article List - Cinephilia").
     * @return a view of the page (if there are no errors) or a redirect to the Home page (if there are).
     */
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView listArticles(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Article List");

            List<ArticleUserListViewModel> articles = this.articleService.findAllArticles()
                    .stream()
                    .map(a -> {
                        ArticleUserListViewModel article = this.modelMapper.map(a, ArticleUserListViewModel.class);
                        if (article.getContent().length() > 100) {
                            article.setContent(article.getContent().substring(0, 100) + "...");
                        }
                        return article;
                    })
                    .collect(Collectors.toList());
            modelAndView.addObject("articles", articles);

            return view("article/list-articles", modelAndView);
        } catch (Exception e) {
            return redirect("home");
        }
    }

    /**
     * Loads a view of all articles. Only for admins.
     * @param modelAndView allows us to attach a list of articles to visualize; also allows us to attach "All Articles" to the title of the page (e.g. "All Articles - Cinephilia").
     * @return a view of the page (if there are no errors) or a redirect to the Home page (if there are).
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allArticles(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "All Articles");
            List<ArticleAdminListViewModel> articles = this.articleService.findAllArticles()
                    .stream()
                    .map(a -> {
                        ArticleAdminListViewModel article = this.modelMapper.map(a, ArticleAdminListViewModel.class);
                        article.setAssociatedMovies(a.getAssociatedMovies()
                                .stream()
                                .map(m -> m.getTitle())
                                .collect(Collectors.toList()));
                        article.setUser(a.getUser().getUsername());
                        return article;
                    })
                    .collect(Collectors.toList());
            modelAndView.addObject("articles", articles);
            return view("article/all-articles", modelAndView);
        } catch (Exception e) {
            return redirect("home");
        }
    }

    /**
     * Loads a view of the Add Article page. Only for admins.
     * @param modelAndView allows us to attach "All Articles" to the page title (e.g. "Add Article - Cinephilia).
     * @return a view of the page (if there are no errors) or a redirect to the All Articles page (if there are).
     */
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addArticle(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Add Article");
            return view("article/add-article", modelAndView);
        } catch (Exception e) {
            return redirect("/articles/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to add an appropriate entity to the database. Only for admins.
     * @param model is the collection of submitted data.
     * @param principal is used to get the username of the current user for the purposes of storing who created the article.
     * @return a view of the All Articles page.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addArticleConfirm(@ModelAttribute(name = "model") ArticleAddBindingModel model, Principal principal, ModelAndView modelAndView) {
        try {
            ArticleServiceModel articleServiceModel = this.modelMapper.map(model, ArticleServiceModel.class);
            articleServiceModel.setAssociatedMovies(
                    this.movieService.findAllMovies()
                            .stream()
                            .filter(m -> model.getAssociatedMovies().contains(m.getId()))
                            .collect(Collectors.toList())
            );
            articleServiceModel.setImageUrl(
                    this.cloudinaryService.uploadImage(model.getImage())
            );
            articleServiceModel.setUser(this.userService.findUserByUsername(principal.getName()));
            this.articleService.addArticle(articleServiceModel);
            return redirect("/articles/all");
        } catch (Exception e) {
            return redirect("/articles/all");
        }

    }

    /**
     * Loads a view of a chosen article's Details page. This includes all associated movies (which have their ids and titles made into pairs).
     * @param id is the id of the articles which is being viewed.
     * @param principal is used to get the username of the current user for the purposes of finding their authorities.
     * @param modelAndView allows us to attach the view model; also allows us to add the article title to the page title (e.g. "Fans boycott Captain Marvel - Cinephilia").
     * @return a view of the page (if there are no errors) or a redirect to either the All Articles page (for admins) or the Articles List page (for other users).
     * @see #findAssociatedMoviePairs(ArticleServiceModel)
     */
    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsArticle(@PathVariable String id, Principal principal, ModelAndView modelAndView) {
        UserAuthoritiesViewModel currentUser = this.findCurrentUser(principal);
        try {
            modelAndView.addObject("currentUser", currentUser);

            ArticleServiceModel articleServiceModel = this.articleService.findArticleByIdAndIncrementViews(id);
            modelAndView.addObject("pageTitle", articleServiceModel.getTitle());

            ArticleDetailsViewModel article = this.modelMapper.map(articleServiceModel, ArticleDetailsViewModel.class);
            article.setUser(articleServiceModel.getUser().getUsername());
            article.setAssociatedMovies(findAssociatedMoviePairs(articleServiceModel));
            modelAndView.addObject("article", article);

            return view("article/details-article", modelAndView);
        } catch (Exception e) {
            if (currentUser.getAuthorities().contains("ROLE_ADMIN")) {
                return redirect("/articles/all");
            }
            return redirect("/articles/list");
        }
    }

    /**
     * Load a view of a chosen article's Edit page. Only for admins.
     * @param id is the id of the article we're editing.
     * @param modelAndView allows us to attach multiple objects to the page.
     * @return a view of the chosen article's Edit page (if there are no errors) or a redirect to the All Articles page.
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editArticle(@PathVariable String id, ModelAndView modelAndView) {
        try {
            ArticleServiceModel articleServiceModel = this.articleService.findArticleById(id);
            ArticleAddBindingModel model = this.modelMapper.map(articleServiceModel, ArticleAddBindingModel.class);
            model.setAssociatedMovies(articleServiceModel.getAssociatedMovies().stream().map(g -> g.getTitle()).collect(Collectors.toList()));
            modelAndView.addObject("pageTitle", "Edit a:" + model.getTitle());
            modelAndView.addObject("article", model);
            modelAndView.addObject("articleId", id);

            return view("article/edit-article", modelAndView);
        } catch (Exception e) {
            return redirect("/articles/all");
        }
    }

    /**
     * Submits the gathered data (from the form on the web page) and attempts to edit the respective article in the database.
     * If no associated movies were selected in the form, associated movies do not get edited. Only for admins.
     * @param id is the id of the article we're editing.
     * @param model is the collection of submitted data.
     * @return a redirect to the article's Details page.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieConfirm(@PathVariable String id, @ModelAttribute ArticleAddBindingModel model) {
        try {
            ArticleServiceModel articleServiceModel = this.modelMapper.map(model, ArticleServiceModel.class);
            try {
                List<MovieServiceModel> movieServiceModels = new ArrayList<>();
                for (String movieId : model.getAssociatedMovies()) {
                    movieServiceModels.add(this.modelMapper.map(this.movieService.findMovieById(movieId), MovieServiceModel.class));
                }
                articleServiceModel.setAssociatedMovies(movieServiceModels);
                this.articleService.editArticleWithEditedAssociatedMovies(id, articleServiceModel);

                return redirect("/articles/details/" + id);
            } catch (Exception e) {
                this.articleService.editArticleWithUneditedAssociatedMovies(id, articleServiceModel);
                return redirect("/articles/details/" + id);
            }
        } catch (Exception e) {
            return redirect("/articles/details/" + id);
        }
    }

    /**
     * Attempts to delete a chosen article. Only for admins.
     * @param id is the id of the article we're deleting.
     * @return a redirect to the All Articles page.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteArticle(@PathVariable String id) {
        try {
            this.articleService.deleteArticle(id);
            return redirect("/articles/all");
        } catch (Exception e) {
            return redirect("/articles/all");
        }
    }

    /**
     * Creates a HashMap with pairs movieTitle-movieId.
     * @param articleServiceModel is the article which has a list of associated movies.
     * @return a HashMap where keys are movie titles and values are their ids.
     */
    private HashMap<String, String> findAssociatedMoviePairs(ArticleServiceModel articleServiceModel) {
        HashMap<String, String> tempAssociatedMovies = new HashMap<>();
        for (MovieServiceModel movie : articleServiceModel.getAssociatedMovies()) {
            tempAssociatedMovies.put(movie.getTitle(), movie.getId());
        }
        return tempAssociatedMovies;
    }
}
