package com.happytails.website.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.happytails.website.config.FileStorageProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ResourceController {
    
    private final FileStorageProperties fileStorageProperties;
    
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        return serveResource(filename);
    }
    
    @GetMapping("/uploads/pet-images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servePetImage(@PathVariable String filename) {
        String fullPath = "pet-images/" + filename;
        return serveResource(fullPath);
    }
    
    private ResponseEntity<Resource> serveResource(String filename) {
        try {
            Path baseDir = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
            Path filePath = baseDir.resolve(filename).normalize();
            
            // Security check to prevent directory traversal attacks
            if (!filePath.startsWith(baseDir)) {
                log.warn("Security warning: Attempted path traversal {}", filename);
                return ResponseEntity.badRequest().build();
            }
            
            log.info("Serving file: {}", filePath);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                log.warn("File not found: {}", filePath);
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            log.error("Error serving file: {}", filename, ex);
            return ResponseEntity.badRequest().build();
        }
    }
} 