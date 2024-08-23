package com.FlickFlow.FlickFlow.movie.repository;

import com.FlickFlow.FlickFlow.movie.dto.MovieDTO;
import com.FlickFlow.FlickFlow.movie.entity.movie;

import java.util.List;

public class MovieApiResponse {

    private String title;
    private String overview;
    private String release_date;
    private String poster_path;
    private double vote_average;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    /////////////////////////
    private int page;
    private List<MovieDTO> results;
    private int total_pages;
    private int total_results;

    // Getters and Setters
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public List<MovieDTO> getResults() { return results; }
    public void setResults(List<MovieDTO> results) { this.results = results; }

    public int getTotalPages() { return total_pages; }
    public void setTotalPages(int total_pages) { this.total_pages = total_pages; }

    public int getTotalResults() { return total_results; }
    public void setTotalResults(int total_results) { this.total_results = total_results; }
}