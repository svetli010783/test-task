package com.test.task.service;

import com.test.task.dto.request.UserRequestDto;
import com.test.task.dto.request.UserRequestUpdateDto;
import com.test.task.dto.response.UserResponseDto;

public interface UserService {
    boolean register(UserRequestDto userRequestDto);

    UserResponseDto getUser(Long id);

    boolean deleteUser(Long id);

    UserResponseDto updateUser(UserRequestUpdateDto userRequestUpdateDto, Long id);

    void addFavouriteMovie(Long userId, Long movieId);

    void removeFavouriteMovie(Long userId, Long movieId);

}
