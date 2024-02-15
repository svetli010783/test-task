package com.test.task.service.impl;

import com.test.task.domain.Movie;
import com.test.task.domain.User;
import com.test.task.dto.request.UserRequestDto;
import com.test.task.dto.request.UserRequestUpdateDto;
import com.test.task.dto.response.UserResponseDto;
import com.test.task.repository.MovieRepository;
import com.test.task.repository.UserRepository;
import com.test.task.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    MovieRepository movieRepository;

    public UserServiceImpl(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public boolean register(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserResponseDto getUser(Long id) {
        var userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: " + id + "not found");
        UserResponseDto userResponseDto = new UserResponseDto(userOptional.get());
        return userResponseDto;
    }

    @Override
    public boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public UserResponseDto updateUser(UserRequestUpdateDto userRequestUpdateDto, Long id) {
        var userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: " + id + "not found");
        User user = userOptional.get();
        if (userRequestUpdateDto.getUsername() != null)
            user.setUsername(userRequestUpdateDto.getUsername());
        if (userRequestUpdateDto.getName() != null)
            user.setName(userRequestUpdateDto.getName());
        userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }

    @Override
    public void addFavouriteMovie(Long userId, Long movieId) {
        var movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "movie with id: " + movieId + "not found");
        Movie movie = movieOptional.get();

        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: " + userId + "not found");
        User user = userOptional.get();
        user.getFavourites().add(movie);
        userRepository.save(user);
    }

    @Override
    public void removeFavouriteMovie(Long userId, Long movieId) {
        var movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "movie with id: " + movieId + "not found");
        Movie movie = movieOptional.get();

        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: " + userId + "not found");
        User user = userOptional.get();
        user.getFavourites().remove(movie);
        userRepository.save(user);
    }
}
