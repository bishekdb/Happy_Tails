package com.happytails.website.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.happytails.website.dto.PetDto;
import com.happytails.website.model.User;
import com.happytails.website.service.PetService;
import com.happytails.website.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {
    
    private final PetService petService;
    private final UserService userService;
    
    @GetMapping
    public String listPets(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByUsername(authentication.getName());
            model.addAttribute("user", currentUser);
        }
        
        model.addAttribute("pets", petService.getAllPets());
        return "pets/list";
    }
    
    @GetMapping("/{id}")
    public String petDetails(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByUsername(authentication.getName());
            model.addAttribute("user", currentUser);
        }
        
        model.addAttribute("pet", petService.getPetById(id));
        return "pets/detail";
    }
    
    @GetMapping("/add")
    public String addPetForm(Model model) {
        model.addAttribute("pet", new PetDto());
        return "pets/add";
    }
    
    @PostMapping("/add")
    public String addPet(@Valid @ModelAttribute("pet") PetDto petDto, 
                         BindingResult result, 
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "pets/add";
        }
        
        User currentUser = userService.findByUsername(authentication.getName());
        petService.savePet(petDto, currentUser);
        
        redirectAttributes.addFlashAttribute("successMessage", "Pet added successfully!");
        return "redirect:/pets";
    }
    
    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes,
                           @RequestParam(value = "redirectTo", required = false, defaultValue = "list") String redirectTo) {
        try {
            User currentUser = userService.findByUsername(authentication.getName());
            petService.deletePet(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Pet deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        // Determine where to redirect based on the redirectTo parameter
        if ("profile".equals(redirectTo)) {
            return "redirect:/profile";
        } else {
            return "redirect:/pets";
        }
    }
} 