package com.example.BMS.services;

import com.example.BMS.models.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MovieService {
    public List<Movie> findAllMovies();
    public Optional<Movie> findMoviesById(long id);

}
