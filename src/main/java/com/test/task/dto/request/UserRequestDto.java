package com.test.task.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@Data
@ToString
public class UserRequestDto {
    @NotBlank(message = "Email is required")
    @Size(max = 255)
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(max = 255)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username should contain only Latin characters and digits")
    private String username;

    @Size(max = 255)
    private String name;
}
