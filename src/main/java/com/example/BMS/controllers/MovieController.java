package com.example.BMS.controllers;

import com.example.BMS.models.Movie;
import com.example.BMS.services.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService)
    {
        this.movieService=movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies()
    {
        return movieService.findAllMovies();
    }
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable long id)
    {
        return movieService.findMoviesById(id).orElse(null);
    }
}
