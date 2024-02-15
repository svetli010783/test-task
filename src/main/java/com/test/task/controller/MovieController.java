package com.test.task.controller;

import com.test.task.domain.enums.LoaderType;
import com.test.task.dto.response.MovieResponseDto;
import com.test.task.service.MovieService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping("movie")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MovieController {
    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Page<MovieResponseDto> getAllMovies(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "15") @Min(1) Integer limit) {
        return movieService.getAllMovies(PageRequest.of(offset, limit));
    }

    @GetMapping("unfavourite")
    public Set<MovieResponseDto> getUnfavouriteMovies(@RequestParam Long userId, @RequestParam LoaderType loaderType) {
        return movieService.getUnfavouriteMovies(userId, loaderType);
    }

}
