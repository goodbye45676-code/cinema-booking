package com.movie.bookingSystem.controller;

import com.movie.bookingSystem.dto.AuthResponse;
import com.movie.bookingSystem.dto.LoginRequest;
import com.movie.bookingSystem.dto.RegisterRequest;
import com.movie.bookingSystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try{
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
