package com.movie.bookingSystem.service;


import com.movie.bookingSystem.dto.MovieDTO;
import com.movie.bookingSystem.model.Movie;
import com.movie.bookingSystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
    public Movie getMovieById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id :" + id));
    }
    public Movie addMovie(MovieDTO dto, MultipartFile image) throws IOException {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setLocation(dto.getLocation());
        movie.setTotalSeats(dto.getTotalSeats());
        movie.setAvailableSeats(dto.getTotalSeats()); // << must be set explicitly
        movie.setPrice(dto.getPrice());
        movie.setShowTime(dto.getShowTime());
        movie.setDescription(dto.getDescription());

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            movie.setImageUrl(imageUrl);
        }

        return movieRepository.save(movie);
    }
    public Movie updateMovie(Long id , MovieDTO dto , MultipartFile image) throws IOException{
        Movie movie = getMovieById(id);

        int seatDifference = dto.getTotalSeats() - movie.getTotalSeats();
        movie.setAvailableSeats(movie.getAvailableSeats() + seatDifference);

        if(image != null && !image.isEmpty()){
            String imageUrl = saveImage(image);
            movie.setImageUrl(imageUrl);
        }
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id){
        if(!movieRepository.existsById(id)){
            throw new RuntimeException("Movie not Found with id :" + id);
        }
        movieRepository.deleteById(id);
    }
    private void mapDtoToEntity(MovieDTO dto , Movie movie){
        movie.setTitle(dto.getTitle());
        movie.setLocation(dto.getLocation());
        movie.setTotalSeats(dto.getTotalSeats());
        movie.setPrice(dto.getPrice());
        movie.setShowTime(dto.getShowTime());
        movie.setDescription(dto.getDescription());
    }
    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/" + fileName;  // << هذا السطر فقط هو ما يُحفظ في DB
    }
}
