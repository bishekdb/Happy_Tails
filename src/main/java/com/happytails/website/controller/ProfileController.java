package com.happytails.website.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.happytails.website.model.User;
import com.happytails.website.service.PetService;
import com.happytails.website.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    
    private final UserService userService;
    private final PetService petService;
    
    @GetMapping
    public String showProfile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        User currentUser = userService.findByUsername(authentication.getName());
        model.addAttribute("user", currentUser);
        model.addAttribute("userPets", petService.getPetsByOwner(currentUser));
        
        return "profile";
    }
} 