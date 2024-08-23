package com.FlickFlow.FlickFlow.movie.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genre") // Ensure this matches your database table name
public class genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genreId;
    private String genreName;
    @ManyToMany(mappedBy = "genres")
    private Set <movie> movies;


    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
