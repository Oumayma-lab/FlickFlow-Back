package com.FlickFlow.FlickFlow.movie.config;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.FlickFlow.FlickFlow.movie.dto.MovieDTO;

public class ApiTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/movies/Details/55";

        try {
            // Make the API request
            String response = restTemplate.getForObject(url, String.class);

            // Print the raw JSON response
            System.out.println("Raw Response: " + response);

            // Parse the JSON response to MovieDTO
            ObjectMapper mapper = new ObjectMapper();
            MovieDTO movie = mapper.readValue(response, MovieDTO.class);

            // Print the parsed MovieDTO object
            System.out.println("Parsed MovieDTO: " + movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
