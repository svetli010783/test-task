package com.test.task.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.domain.Movie;
import com.test.task.domain.PersistentObject;
import com.test.task.domain.User;
import com.test.task.domain.enums.LoaderType;
import com.test.task.dto.response.MovieResponseDto;
import com.test.task.repository.MovieRepository;
import com.test.task.repository.UserRepository;
import com.test.task.service.MovieService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieServiceImpl implements MovieService {
    final MovieRepository movieRepository;
    final UserRepository userRepository;
    final Set<Movie> localMovies = new HashSet<>();
    Long collectionNumber = 0L;
    @PersistenceContext
    EntityManager entityManager;

    public MovieServiceImpl(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<MovieResponseDto> getAllMovies(PageRequest pageRequest) {
        Page<Movie> movies = movieRepository.findAll(pageRequest);
        Page<MovieResponseDto> movieResponseDtos = movies.map(MovieResponseDto::new);
        return movieResponseDtos;
    }

    @Scheduled(fixedRate = 10800000)
    private void collectMovies() throws IOException, InterruptedException {
        do {
            collectionNumber++;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.themoviedb.org/3/discover/movie?include_video=false&page=" + collectionNumber))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjM2RkNDJhZGE0NDYxODU0YjNkMDRkNmU5NGE1ZWZkNyIsInN1YiI6IjY1YzRkMTkwMGMyNzEwMDBjOTc3NjRlOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.o9Y76TBMOPam3O2C30BpFFyniQo2O1dm_VUZH6FEWnA")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info(response.body());
            parseToEntity(response.body());
        } while (collectionNumber % 5 != 0);
    }

    private void parseToEntity(String body) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode rootNode = mapper.readTree(body);
            JsonNode resultsNode = rootNode.get("results");

            List<Movie> movies = new ArrayList<>();

            for (var movieNode : resultsNode) {
                String title = movieNode.get("title").asText();
                String posterPath = movieNode.get("poster_path").asText();

                movieRepository.findByTitle(title).ifPresentOrElse(x -> log.info("{} is already saved", title), () -> {
                    Movie movie = new Movie();
                    movie.setTitle(title);
                    movie.setPosterPath(posterPath);
                    movies.add(movie);
                    localMovies.add(movie);
                });
            }
            movieRepository.saveAll(movies);
        } catch (JsonProcessingException e) {
            log.error("error while parsing", e);
        }

    }

    @Override
    public Set<MovieResponseDto> getUnfavouriteMovies(Long userId, LoaderType loaderType) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: " + userId + "not found");
        Set<Movie> userFavourites = userOptional.get().getFavourites();
        switch (loaderType) {
            case SQL:
                Set<Movie> unfavouritesSql = movieRepository.findAllUnfavourites(userFavourites.stream().map(PersistentObject::getId).collect(Collectors.toSet()));
                Set<MovieResponseDto> movieResponseDtosSql = unfavouritesSql.stream().map(MovieResponseDto::new).collect(Collectors.toSet());
                return movieResponseDtosSql;
            case IN_MEMORY:
                Set<Movie> localMoviesCopy = new HashSet<>(Set.copyOf(localMovies));
                localMoviesCopy.removeAll(userFavourites);
                Set<Movie> unfavouritesLocal = localMoviesCopy;
                Set<MovieResponseDto> movieResponseDtosLocal = unfavouritesLocal.stream().map(MovieResponseDto::new).collect(Collectors.toSet());
                return movieResponseDtosLocal;
        }
        return Set.of();
    }
}
