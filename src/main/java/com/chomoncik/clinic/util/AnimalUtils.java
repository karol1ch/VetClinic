package com.chomoncik.clinic.util;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;


public class AnimalUtils {

    public static AnimalResponseDTO convertAnimalToAnimalResponseDTO(Animal animal) {
        if (animal.getOwner().isEmpty()) {
            return AnimalResponseDTO.builder()
                    .animalId(animal.getAnimalId())
                    .name(animal.getAnimalName())
                    .species(animal.getSpecies())
                    .birthYear(animal.getBirthYear())
                    .deathYear(animal.getDeathYear())
                    .owner(null)
                    .build();
        } else {
            return AnimalResponseDTO.builder()
                    .animalId(animal.getAnimalId())
                    .name(animal.getAnimalName())
                    .species(animal.getSpecies())
                    .birthYear(animal.getBirthYear())
                    .deathYear(animal.getDeathYear())
                    .owner(animal.getOwner().get().getName())
                    .build();
        }
    }
}
