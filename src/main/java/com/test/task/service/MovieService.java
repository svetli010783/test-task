package com.test.task.service;

import com.test.task.domain.enums.LoaderType;
import com.test.task.dto.response.MovieResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;

public interface MovieService {
    Page<MovieResponseDto> getAllMovies(PageRequest pageRequest);

    Set<MovieResponseDto> getUnfavouriteMovies(Long userId, LoaderType loaderType);
}
