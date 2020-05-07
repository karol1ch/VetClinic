package com.chomoncik.clinic.util;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;

public class AnimalUtils {

    public static AnimalResponseDTO convertAnimalToAnimalResponseDTO(Animal animal) {
        return AnimalResponseDTO.builder()
                .animalId(animal.getAnimalId())
                .name(animal.getName())
                .species(animal.getSpecies())
                .birthYear(animal.getBirthYear())
                .deathYear(animal.getDeathYear())
                .owner(null)
                .build();
    }
}
