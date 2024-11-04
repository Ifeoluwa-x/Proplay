package com.soccerapp.app.dto;

import com.soccerapp.app.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Integer playerId;
    private String availability;
    private String position;
    private String skillLevel;
    private String playerBio;

    private String fname;
    private String lname;
    private String email;
    private int age;
    private String sex;
    private String team;
}
