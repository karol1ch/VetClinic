package com.chomoncik.clinic.service;

import com.chomoncik.clinic.exception.ResourceNotFoundException;
import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.repository.AnimalRepository;
import com.chomoncik.clinic.util.AnimalUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public Animal addAnimal(AnimalRequestDTO animalRequestDTO) {
        Animal animal = new Animal(animalRequestDTO);
        return animalRepository.save(animal);
    }

    public AnimalResponseDTO getAnimalResponseDTOById(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Animal", "animalId", animalId)
                );
        return AnimalUtils.convertAnimalToAnimalResponseDTO(animal);
    }
}
