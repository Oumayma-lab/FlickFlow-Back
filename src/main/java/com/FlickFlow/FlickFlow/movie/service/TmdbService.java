package com.FlickFlow.FlickFlow.movie.service;

import com.FlickFlow.FlickFlow.movie.dto.MovieDTO;
import com.FlickFlow.FlickFlow.movie.repository.MovieApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Service
public class TmdbService {

    private final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "ac6793f3434a0391c5ada387c27b12c3";

    private final RestTemplate restTemplate;

    public TmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchMovieData(Integer tmdbId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment("movie", tmdbId.toString())
                .queryParam("api_key", API_KEY)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    public MovieDTO getMovieDetails(String tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "?api_key=" + API_KEY;
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        // Convert JsonNode to MovieDTO
        return parseJsonNodeToMovieDTO(response);
    }

    private MovieDTO parseJsonNodeToMovieDTO(JsonNode jsonNode) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(jsonNode.get("id").asInt());
        movieDTO.setTitle(jsonNode.get("title").asText());
        movieDTO.setOverview(jsonNode.get("overview").asText());
        movieDTO.setRelease_date(LocalDate.parse(jsonNode.get("release_date").asText()));
        movieDTO.setPoster_path(jsonNode.get("poster_path").asText());
        // Handle genres
        // Note: Parsing genres might require additional handling depending on the API response structure
        return movieDTO;
    }




    public MovieDTO getMovieById(int tmdbId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + tmdbId)
                .queryParam("api_key", API_KEY)
                .queryParam("language", "en-US")
                .toUriString();
        return restTemplate.getForObject(url, MovieDTO.class);
    }

    public MovieDTO getPopularMovies() {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/movie/popular")
                .queryParam("api_key", API_KEY)
                .toUriString();
        return restTemplate.getForObject(url, MovieDTO.class);
    }


    public String getMovieDetails(Integer tmdbId) {
        String url = BASE_URL + tmdbId + "?api_key=" + API_KEY;
        return restTemplate.getForObject(url, String.class);
    }

    public MovieApiResponse getMovieDetailsByTmdbId(int tmdbId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/movie/" + tmdbId + "?api_key=" + API_KEY;
        return restTemplate.getForObject(url, MovieApiResponse.class);
    }


//    public String fetchMovieData(Integer tmdbId) {
//        // Implementation for fetching raw data
//        return null; // Replace with actual logic
//    }
}