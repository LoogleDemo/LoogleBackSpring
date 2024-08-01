package com.loogle.loogle_spring.repositories;

import com.loogle.loogle_spring.domain.Colour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColourRepository extends JpaRepository<Colour, Long> {
    Optional<Colour> findByColourName(String colourName);

}