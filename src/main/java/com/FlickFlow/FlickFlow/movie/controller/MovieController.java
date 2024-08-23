package com.FlickFlow.FlickFlow.movie.controller;

import com.FlickFlow.FlickFlow.movie.entity.genre;
import com.FlickFlow.FlickFlow.movie.entity.movie;
import com.FlickFlow.FlickFlow.movie.repository.MovieApiResponse;
import com.FlickFlow.FlickFlow.movie.service.MovieService;
import com.FlickFlow.FlickFlow.movie.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    private final TmdbService tmdbService;

    public MovieController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/trending")
    public ResponseEntity<List<movie>> getTrendingMovies() {
        List<movie> movies = movieService.getTrendingMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<genre>> getAllGenres() {
        List<genre> genres = movieService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/by-genre/{genreId}")
    public ResponseEntity<List<movie>> getMoviesByGenre(@PathVariable Integer genreId) {
        List<movie> movies = movieService.getMoviesByGenre(genreId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<movie> getMovieById(@PathVariable Integer id) {
        movie movie = movieService.getMovieById(id);
        return movie != null ? ResponseEntity.ok(movie) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<movie>> searchMovies(@RequestParam String query) {
        List<movie> movies = movieService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/dump/{tmdbId}")
    public String dumpMovie(@PathVariable int tmdbId) {
        movieService.dumpMovieData(tmdbId);
        return "Movie data dumped successfully!";
    }

    @GetMapping("/dump/popular")
    public String dumpPopularMovies() {
        movieService.dumpPopularMovies();
        return "Popular movie data dumped successfully!";
    }

    @GetMapping("/movie/{tmdbId}")
    public String getMovieByTMDBId(@PathVariable Integer tmdbId) {
        return tmdbService.getMovieDetails(tmdbId);
    }


    @GetMapping("details/{tmdbId}")
    public ResponseEntity<MovieApiResponse> getMovieDetails(@PathVariable int tmdbId) {
        MovieApiResponse movieDetails = tmdbService.getMovieDetailsByTmdbId(tmdbId);
        return ResponseEntity.ok(movieDetails);
    }




}