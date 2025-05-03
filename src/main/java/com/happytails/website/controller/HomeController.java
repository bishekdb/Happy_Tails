package com.happytails.website.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.happytails.website.model.Pet;
import com.happytails.website.model.User;
import com.happytails.website.service.PetService;
import com.happytails.website.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final UserService userService;
    private final PetService petService;
    
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByUsername(authentication.getName());
            model.addAttribute("user", currentUser);
        }
        
        // Add all pets to the model (for existing functionality)
        model.addAttribute("pets", petService.getAllPets());
        
        // Get the 3 latest pet listings for the "Meet Our Latest Pets" section
        List<Pet> latestPets = petService.getLatestPets(3);
        model.addAttribute("latestPets", latestPets);
        
        // Check which pets were added in the last 24 hours
        LocalDateTime oneDayAgo = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        for (Pet pet : latestPets) {
            if (pet.getCreatedAt() != null && pet.getCreatedAt().isAfter(oneDayAgo)) {
                model.addAttribute("isNew_" + pet.getId(), true);
            }
        }
        
        return "index";
    }
} 