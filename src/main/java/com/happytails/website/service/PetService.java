package com.happytails.website.service;

import java.util.List;

import com.happytails.website.dto.PetDto;
import com.happytails.website.model.Pet;
import com.happytails.website.model.User;

public interface PetService {
    Pet savePet(PetDto petDto, User owner);
    List<Pet> getAllPets();
    List<Pet> getPetsByOwner(User owner);
    Pet getPetById(Long id);
    void deletePet(Long id, User currentUser);
    List<Pet> getLatestPets(int limit);
} 