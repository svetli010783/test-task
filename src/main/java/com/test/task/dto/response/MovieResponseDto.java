package com.test.task.dto.response;

import com.test.task.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDto {
    private Long id;
    private String title;
    private String posterPath;

    public MovieResponseDto(Movie movie) {
        this(movie.getId(), movie.getTitle(), movie.getPosterPath());
    }
}
