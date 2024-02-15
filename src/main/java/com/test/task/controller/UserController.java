package com.test.task.controller;

import com.test.task.dto.request.UserRequestDto;
import com.test.task.dto.request.UserRequestUpdateDto;
import com.test.task.dto.response.UserResponseDto;
import com.test.task.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void register(@Valid @RequestBody UserRequestDto request) {
        userService.register(request);
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public UserResponseDto read(@RequestParam Long id) {
        return userService.getUser(id);
    }

    @PatchMapping
    public UserResponseDto update(@Valid @RequestBody UserRequestUpdateDto request, @RequestParam Long id) {
        return userService.updateUser(request, id);
    }

    @PostMapping("/favourite")
    public void addFavouriteMovie(@RequestParam Long userId, @RequestParam Long movieId) {
        userService.addFavouriteMovie(userId, movieId);
    }

    @DeleteMapping("/favourite")
    public void removeFavouriteMovie(@RequestParam Long userId, @RequestParam Long movieId) {
        userService.removeFavouriteMovie(userId, movieId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
