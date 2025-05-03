package com.happytails.website.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.happytails.website.config.FileStorageProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    
    public String storeFile(MultipartFile file) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(fileStorageProperties.getUploadDir())
                    .toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);
            
            // Generate a unique file name to prevent conflicts
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            
            // Copy the file to the target location
            Path targetLocation = uploadDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf("."));
        } else {
            return "";
        }
    }
} 