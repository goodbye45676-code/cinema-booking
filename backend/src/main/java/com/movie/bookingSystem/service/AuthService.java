package com.movie.bookingSystem.service;

import com.movie.bookingSystem.config.JwtUtil;
import com.movie.bookingSystem.dto.AuthResponse;
import com.movie.bookingSystem.dto.LoginRequest;
import com.movie.bookingSystem.dto.RegisterRequest;
import com.movie.bookingSystem.model.User;
import com.movie.bookingSystem.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository , PasswordEncoder passwordEncoder , JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("EMAIL ALREADY EXISTS");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return  new AuthResponse(token , user.getRole().name() , user.getName() ,user.getId());
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password "));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user.getEmail() , user.getRole().name());
        return new AuthResponse(token , user.getRole().name() , user.getName() ,user.getId());

    }

}
