package com.chomoncik.clinic.converter;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.dto.AnimalResponseDTO;


public class AnimalConverter {

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
