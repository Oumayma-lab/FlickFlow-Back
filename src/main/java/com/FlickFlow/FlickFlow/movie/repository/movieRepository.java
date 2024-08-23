package com.FlickFlow.FlickFlow.movie.repository;
import com.FlickFlow.FlickFlow.movie.entity.movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface movieRepository extends JpaRepository<movie, Integer> {
//    movie findByTmdbId(Integer tmdbId);
//    Page<movie> findAllByGenreIdsContains(int genreId, Pageable pageable);
    Optional<movie> findByTmdbId(Integer tmdbId);
//    Page<movie> findAllByGenres_Id(int genreId, Pageable pageable);
//    List<movie> findByGenreId(@Param("genreId") Integer genreId);


    @Query("SELECT m FROM movie m JOIN m.genres g WHERE g.genreId = :genreId")
    List<movie> findByGenreId(@Param("genreId") Integer genreId);

    @Query("SELECT m FROM movie m WHERE m.title LIKE %:query%")
    List<movie> searchByTitle(@Param("query") String query);

    @Query("SELECT m.tmdbId FROM movie m")
    List<Integer> findAllTmdbIds();

}
