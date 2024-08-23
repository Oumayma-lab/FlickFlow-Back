package com.FlickFlow.FlickFlow.movie.service;
import com.FlickFlow.FlickFlow.movie.dto.GenreDTO;
import com.FlickFlow.FlickFlow.movie.dto.MovieDTO;
import com.FlickFlow.FlickFlow.movie.dto.MovieSearchResult;
import com.FlickFlow.FlickFlow.movie.entity.genre;
import com.FlickFlow.FlickFlow.movie.entity.movie;
import com.FlickFlow.FlickFlow.movie.repository.MovieApiResponse;
import com.FlickFlow.FlickFlow.movie.repository.movieRepository;
import com.FlickFlow.FlickFlow.movie.repository.genreRepository;
import com.FlickFlow.FlickFlow.movie.results.PopularMoviesResponse;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.FlickFlow.FlickFlow.movie.config.TmdbConfig;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.awt.print.Pageable;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {


    @Autowired
    private movieRepository movieRepository;
    @Autowired
    private genreRepository genreRepository;
    @Autowired
    private RestTemplate restTemplate;
    private final TmdbService tmdbService;

    private final String TMDB_API_KEY = "2c74ca902e8d7b6b4eb2ddadb56bfa1e";
    private final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final String TMDB_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=" + TMDB_API_KEY + "&page=";
    private final String TMDB_DETAIL_URL = "https://api.themoviedb.org/3/movie/";
    private static final float POPULARITY_THRESHOLD = 1.5f;
    public MovieService(movieRepository movieRepository, TmdbService tmdbService) {
        this.movieRepository = movieRepository;
        this.tmdbService = tmdbService;
    }

    public void dumpPopularMovies() {
        int page = 1;
        boolean morePages = true;

        while (morePages) {
            String url = TMDB_POPULAR_URL + page;
            PopularMoviesResponse response = restTemplate.getForObject(url, PopularMoviesResponse.class);

            if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                for (MovieDTO movieDTO : response.getResults()) {
                    dumpMovieData(movieDTO.getId());
                }
                page++;
                if (page > response.getTotal_pages()) {
                    morePages = false;
                }
            } else {
                morePages = false;
            }
        }
    }





    public void dumpMovieData(int tmdbId) {
        String url = TMDB_DETAIL_URL + tmdbId + "?api_key=" + TMDB_API_KEY;
        MovieDTO movieDTO = restTemplate.getForObject(url, MovieDTO.class);

        if (movieDTO != null && movieRepository.findByTmdbId(tmdbId).isEmpty()) {
            movie movie = new movie();
            movie.setTmdbId(movieDTO.getId());
            movie.setTitle(movieDTO.getTitle());
            movie.setOverview(movieDTO.getOverview());
            movie.setReleaseDate(movieDTO.getRelease_date());
            movie.setRating(movieDTO.getVote_average());
            movie.setPosterPath(movieDTO.getPoster_path());
            movie.setBackdropPath(movieDTO.getBackdrop_path());
            movie.setOriginalLanguage(movieDTO.getOriginal_language());
            movie.setOriginalTitle(movieDTO.getOriginal_title());
            movie.setPopularity(movieDTO.getPopularity());
            movie.setVoteAverage(movieDTO.getVote_average());
            movie.setVoteCount(movieDTO.getVote_count());
            movie.setVideo(movieDTO.isVideo());

            Set<genre> genres = new HashSet<>();
            for (GenreDTO genreDTO : movieDTO.getGenres()) {
                genre genre = genreRepository.findById(genreDTO.getId()).orElse(null);
                if (genre == null) {
                    genre = new genre();
                    genre.setGenreId(genreDTO.getId());
                    genre.setGenreName(genreDTO.getName());
                    genreRepository.save(genre);
                }
                genres.add(genre);
            }
            movie.setGenres(genres);

            movieRepository.save(movie);
        }
    }

    public List<movie> getTrendingMovies() {
        // Implement logic to fetch trending movies if needed
        return movieRepository.findAll(); // For demo purposes
    }

    public List<genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public List<movie> getMoviesByGenre(Integer genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    public movie getMovieById(Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<movie> searchMovies(String query) {
        return movieRepository.searchByTitle(query);
    }

}