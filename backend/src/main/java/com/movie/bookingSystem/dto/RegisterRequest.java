package com.movie.bookingSystem.dto;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;

}
