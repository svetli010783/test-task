package com.test.task.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE movie SET deleted_at = NOW() WHERE id=?")
public class Movie extends PersistentObject {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "poster_path", nullable = false)
    private String posterPath;
    @ManyToMany(mappedBy = "favourites")
    private Set<User> favoritedByUsers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title) && Objects.equals(posterPath, movie.posterPath) && Objects.equals(favoritedByUsers, movie.favoritedByUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, posterPath, favoritedByUsers);
    }
}
