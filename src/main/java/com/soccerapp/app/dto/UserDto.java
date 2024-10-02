package com.soccerapp.app.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private int age;
    private String position;
    private String skillLevel;
    private String availability;
    private String location;
    private String sex;
    private String team;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
