package com.FlickFlow.FlickFlow.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreDTO {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public GenreDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
