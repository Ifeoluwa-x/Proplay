package com.soccerapp.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.OnMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "fname is required.")
    private String fname;
    @NotBlank(message = "lname is required.")
    private String lname;
    @NotBlank(message = "email is required.")
    private String email;
    @NotBlank(message = "password is required.")
    private String password;
    @Min(value = 18, message = "Age must be at least 18.")
    @Max(value = 120, message = "Age cannot exceed 120.")
    private int age;
    @NotBlank(message = "Location is required.")
    private String location;
    @NotBlank(message = "sex is required.")
    private String sex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
