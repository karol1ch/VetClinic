package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.AnimalRepository;
import com.chomoncik.clinic.util.AnimalUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;

    public Animal addAnimal(AnimalRequestDTO animalRequestDTO) {
        Animal animal = new Animal(animalRequestDTO);
        log.info("Save animal with id={}.", animal.getAnimalId());
        return animalRepository.save(animal);
    }

    public Optional<AnimalResponseDTO> getAnimalResponseDTOById(Long animalId) {
        Optional<Animal> animal = getAnimalById(animalId);
        return animal.map(AnimalUtils::convertAnimalToAnimalResponseDTO);
    }

    public Optional<Animal> getAnimalById(Long animalId) {
        return animalRepository.findById(animalId);
    }

    public List<AnimalResponseDTO> getAllAnimals() {
        List<Animal> animalList = animalRepository.findAll();
        return animalList.stream()
                .map(AnimalUtils::convertAnimalToAnimalResponseDTO)
                .collect(Collectors.toList());
    }

    public AnimalResponseDTO addOwner(Animal animal, Person owner) {
        Animal animalWithOwner = createAnimalWithOwner(animal, owner);
        animalRepository.save(animalWithOwner);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(animalWithOwner);
    }

    public AnimalResponseDTO addDeathYear(Animal animal, int deathYear) {
        Animal deathAnimal = Animal.builder()
                .animalId(animal.getAnimalId())
                .animalName(animal.getAnimalName())
                .species(animal.getSpecies())
                .birthYear(animal.getBirthYear())
                .deathYear(deathYear)
                .owner(animal.getOwner().orElse(null))
                .build();
        animalRepository.save(deathAnimal);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(deathAnimal);
    }

    public AnimalResponseDTO removeOwnerFromAnimal(Animal animal) {
        if (animal.getOwner().isEmpty()) {
            return AnimalUtils.convertAnimalToAnimalResponseDTO(animal);
        } else {
            Animal animalWithoutOwner = createAnimalWithOwner(animal, null);
            animalRepository.save(animalWithoutOwner);
            return AnimalUtils.convertAnimalToAnimalResponseDTO(animalWithoutOwner);
        }
    }

    public void deleteAnimalById(Long animalId) {
            animalRepository.deleteById(animalId);
    }

    public boolean checkIfAnimalWithIdExist(Long animalId) {
        return animalRepository.existsById(animalId);
    }

    private Animal createAnimalWithOwner(Animal animal, Person owner) {
        return Animal.builder()
                .animalId(animal.getAnimalId())
                .animalName(animal.getAnimalName())
                .species(animal.getSpecies())
                .birthYear(animal.getBirthYear())
                .deathYear(animal.getDeathYear())
                .owner(owner)
                .build();
    }

}
