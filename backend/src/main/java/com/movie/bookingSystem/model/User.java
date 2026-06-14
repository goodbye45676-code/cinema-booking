package com.movie.bookingSystem.model;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;

import javax.management.relation.Role;

@Entity
@Table(name= "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 100)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private  String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    public enum Role{
        ADMIN , USER
    }
}
