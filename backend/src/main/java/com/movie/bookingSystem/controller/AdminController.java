package com.movie.bookingSystem.controller;


import com.movie.bookingSystem.dto.MovieDTO;
import com.movie.bookingSystem.model.Movie;
import com.movie.bookingSystem.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final MovieService movieService;
    private final ObjectMapper objectMapper;

    public AdminController(MovieService movieService , ObjectMapper objectMapper){
        this.movieService = movieService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = "Multipart/form-data")
    public ResponseEntity<?> addMovie(
            @RequestPart("movie") String movieJson,
            @RequestPart(value = "image" , required = false)MultipartFile image){
        try{
            MovieDTO dto = objectMapper.readValue(movieJson, MovieDTO.class);
            Movie movie = movieService.addMovie(dto , image);
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping(value = "/{id}" , consumes = "multipart/form-data")
    public ResponseEntity<?> updateMovie(
            @PathVariable Long id,
            @RequestPart("movie") String movieJson,
            @RequestPart(value = "image" , required = false) MultipartFile image
    ){
        try{
            MovieDTO dto = objectMapper.readValue(movieJson , MovieDTO.class);
            Movie movie = movieService.updateMovie(id , dto ,image);
            return ResponseEntity.ok(movie);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        try{
            movieService.deleteMovie(id);
            return ResponseEntity.ok("Movie deleted successfully");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
