package com.test.task.dto.response;

import com.test.task.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String username;
    private String name;

    public UserResponseDto(User user) {
        this(user.getId(), user.getEmail(), user.getUsername(), user.getName());
    }

}
