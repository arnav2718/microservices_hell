package com.example.ecomm_orderservice.entity;


import com.example.ecomm_orderservice.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    private String name;
    private UserRole role;

    @Lob
    @Column(columnDefinition = "longBlob")
    private byte[] img;

    // Getters and Setters
}
