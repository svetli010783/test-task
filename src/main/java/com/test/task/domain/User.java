package com.test.task.domain;


import com.test.task.dto.request.UserRequestDto;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id=?")
public class User extends PersistentObject {
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "favourites")
    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> favourites = new HashSet<>();
    @Column(name = "name")
    private String name;

    public User(UserRequestDto userRequestDto) {
        this(userRequestDto.getEmail(), userRequestDto.getUsername(), userRequestDto.getName());
    }

    public User(String email, String username, String name) {
        this.email = email;
        this.username = username;
        this.name = name;
    }
}
