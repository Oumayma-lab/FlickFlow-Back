package com.FlickFlow.FlickFlow.movie.results;
import com.FlickFlow.FlickFlow.movie.dto.MovieDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PopularMoviesResponse {
    private List<MovieDTO> results;
    private int page;

    public void setResults(List<MovieDTO> results) {
        this.results = results;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    private int total_pages;
    private int total_results;

}
