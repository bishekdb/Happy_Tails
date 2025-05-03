package com.happytails.website.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happytails.website.dto.UserRegistrationDto;
import com.happytails.website.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        
        // Check if passwords match
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
        }
        
        // Check if username is already taken
        if (userService.existsByUsername(registrationDto.getUsername())) {
            result.rejectValue("username", null, "Username is already taken");
        }
        
        // Check if email is already registered
        if (userService.existsByEmail(registrationDto.getEmail())) {
            result.rejectValue("email", null, "Email is already registered");
        }
        
        if (result.hasErrors()) {
            return "register";
        }
        
        // Register new user
        userService.registerNewUser(registrationDto);
        
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
        return "redirect:/login";
    }
} 