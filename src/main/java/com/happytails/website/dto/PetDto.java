package com.happytails.website.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    
    @NotBlank(message = "Pet name is required")
    private String name;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @NotNull(message = "Age is required")
    @Positive(message = "Age must be positive")
    private Integer age;
    
    @NotBlank(message = "Breed is required")
    private String breed;
    
    @NotNull(message = "Pet image is required")
    private MultipartFile image;
} 