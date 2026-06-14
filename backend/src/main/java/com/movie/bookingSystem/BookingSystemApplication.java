package com.movie.bookingSystem;

import com.movie.bookingSystem.model.User;
import com.movie.bookingSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingSystemApplication.class, args);
	}
	@Bean
	public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			if (!userRepository.existsByEmail("admin@booking.com")) {
				User admin = new User();
				admin.setName("Admin");
				admin.setEmail("admin@booking.com");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole(User.Role.ADMIN);
				userRepository.save(admin);
			}
		};
	}
}
