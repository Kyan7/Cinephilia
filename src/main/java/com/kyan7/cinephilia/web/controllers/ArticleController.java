package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.ArticleAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.ArticleServiceModel;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.domain.models.view.ArticleAdminListViewModel;
import com.kyan7.cinephilia.domain.models.view.ArticleDetailsViewModel;
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

import java.io.IOException;
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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allArticles(ModelAndView modelAndView) {
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
        return super.view("article/all-articles", modelAndView);
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addArticle(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Add Article");
        //modelAndView.addObject("associatedMovies",
        //        this.a.findAllGenresOrderByName()
        //                .stream()
        //                .map(g -> this.modelMapper.map(g, GenreViewModel.class))
        //                .collect(Collectors.toList()));
        return super.view("article/add-article", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addArticleConfirm(@ModelAttribute(name = "model") ArticleAddBindingModel model, Principal principal, ModelAndView modelAndView) throws IOException {
        try {
            model.getAssociatedMovies().forEach(m -> System.out.println("movie:" + m));
            ArticleServiceModel articleServiceModel = this.modelMapper.map(model, ArticleServiceModel.class);
            articleServiceModel.setAssociatedMovies(
                    this.movieService.findAllMovies()
                            .stream()
                            .filter(m -> model.getAssociatedMovies().contains(m.getId()))
                            .collect(Collectors.toList())
            );
            System.out.println("Attempting to upload");
            articleServiceModel.setImageUrl(
                    this.cloudinaryService.uploadImage(model.getImage())
            );
            articleServiceModel.setUser(this.userService.findUserByUsername(principal.getName()));
            System.out.println(articleServiceModel.getTitle());
            System.out.println(articleServiceModel.getUser());
            System.out.println(articleServiceModel.getContent());
            for (var am : articleServiceModel.getAssociatedMovies()
                 ) {
                System.out.println(am.getTitle());
            }
            System.out.println(articleServiceModel.getImageUrl());
            this.articleService.addArticle(articleServiceModel);
            return super.redirect("/articles/all");
        } catch (Exception e) {
            return super.redirect("/articles/all");
        }

    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsArticle(@PathVariable String id, Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("currentUser", findCurrentUser(principal));

        ArticleServiceModel articleServiceModel = this.articleService.findArticleByIdAndIncrementViews(id);
        modelAndView.addObject("pageTitle", articleServiceModel.getTitle());

        ArticleDetailsViewModel article = this.modelMapper.map(articleServiceModel, ArticleDetailsViewModel.class);
        article.setUser(articleServiceModel.getUser().getUsername());
        HashMap<String, String> tempAssociatedMovies = new HashMap<>();
        for (MovieServiceModel movie : articleServiceModel.getAssociatedMovies()
             ) {
            tempAssociatedMovies.put(movie.getTitle(), movie.getId());
        }
        article.setAssociatedMovies(tempAssociatedMovies);
        modelAndView.addObject("article", article);

        return super.view("article/details-article", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editArticle(@PathVariable String id, ModelAndView modelAndView) {
        ArticleServiceModel articleServiceModel = this.articleService.findArticleById(id);
        ArticleAddBindingModel model = this.modelMapper.map(articleServiceModel, ArticleAddBindingModel.class);
        model.setAssociatedMovies(articleServiceModel.getAssociatedMovies().stream().map(g -> g.getTitle()).collect(Collectors.toList()));
        modelAndView.addObject("pageTitle", "Edit a:" + model.getTitle());
        modelAndView.addObject("article", model);
        modelAndView.addObject("articleId", id);

        return super.view("article/edit-article", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieConfirm(@PathVariable String id, @ModelAttribute ArticleAddBindingModel model) {
        try {
            ArticleServiceModel articleServiceModel = this.modelMapper.map(model, ArticleServiceModel.class);
            try {
                List<MovieServiceModel> movieServiceModels = new ArrayList<>();
                for (String movieId : model.getAssociatedMovies()
                ) {
                    movieServiceModels.add(this.modelMapper.map(this.movieService.findMovieById(movieId), MovieServiceModel.class));
                }
                articleServiceModel.setAssociatedMovies(movieServiceModels);
                this.articleService.editArticleWithEditedAssociatedMovies(id, articleServiceModel);
                return super.redirect("/articles/details/" + id);
            } catch (Exception e) {
                this.articleService.editArticleWithUneditedAssociatedMovies(id, articleServiceModel);
                return super.redirect("/articles/details/" + id);
            }
        } catch (Exception e) {
            return super.redirect("/articles/details/" + id);
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteArticle(@PathVariable String id) {
        try {
            this.articleService.deleteArticle(id);
            return super.redirect("/articles/all");
        } catch (Exception e) {
            return super.redirect("/articles/all");
        }
    }
}
