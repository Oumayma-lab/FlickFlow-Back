package com.FlickFlow.FlickFlow.movie.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "tmdb")
@Component
public class TmdbConfig {

    @Value("${tmdb.api.key}")
    private  String apiKey;

    @Value("${tmdb.api.url}")
    private  String apiUrl;


    public  String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public  String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}