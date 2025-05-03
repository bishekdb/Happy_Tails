package com.happytails.website.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happytails.website.dto.PetDto;
import com.happytails.website.model.Pet;
import com.happytails.website.model.User;
import com.happytails.website.repository.PetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Pet savePet(PetDto petDto, User owner) {
        log.info("Saving new pet: {}, owned by: {}", petDto.getName(), owner.getUsername());
        
        // Store the pet image and get the filename
        String fileName = fileStorageService.storeFile(petDto.getImage());
        log.info("Stored pet image with filename: {}", fileName);
        
        // Create and save the pet entity
        Pet pet = Pet.builder()
                .name(petDto.getName())
                .gender(petDto.getGender())
                .age(petDto.getAge())
                .breed(petDto.getBreed())
                .imageUrl(fileName)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .build();
        
        Pet savedPet = petRepository.save(pet);
        log.info("Successfully saved pet with ID: {}", savedPet.getId());
        return savedPet;
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public List<Pet> getPetsByOwner(User owner) {
        return petRepository.findByOwner(owner);
    }

    @Override
    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
    }
    
    @Override
    public void deletePet(Long id, User currentUser) {
        Pet pet = getPetById(id);
        
        // Check if the current user is the owner of the pet
        if (!pet.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this pet");
        }
        
        // Delete the pet
        petRepository.delete(pet);
    }
    
    @Override
    public List<Pet> getLatestPets(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return petRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
} 