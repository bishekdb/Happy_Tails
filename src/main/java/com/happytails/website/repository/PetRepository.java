package com.happytails.website.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happytails.website.model.Pet;
import com.happytails.website.model.User;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User owner);
    
    // Find latest pets by creation date (newest first)
    List<Pet> findAllByOrderByCreatedAtDesc(Pageable pageable);
} 