package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.dto.AnimalRequestDTO;
import com.chomoncik.clinic.model.dto.AnimalResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.AnimalRepository;
import com.chomoncik.clinic.converter.AnimalConverter;
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
        Animal createdAnimal = animalRepository.save(new Animal(animalRequestDTO));
        log.info("Save animal with id={}.", createdAnimal.getAnimalId());
        return createdAnimal;
    }

    public Optional<AnimalResponseDTO> getAnimalResponseDTOById(Long animalId) {
        Optional<Animal> animal = getAnimalById(animalId);
        return animal.map(AnimalConverter::convertAnimalToAnimalResponseDTO);
    }

    public Optional<Animal> getAnimalById(Long animalId) {
        log.info("Get animal with id={}.", animalId);
        return animalRepository.findById(animalId);
    }

    public List<AnimalResponseDTO> getAllAnimals() {
        List<Animal> animalList = animalRepository.findAll();
        return animalList.stream()
                .map(AnimalConverter::convertAnimalToAnimalResponseDTO)
                .collect(Collectors.toList());
    }

    public AnimalResponseDTO addOwner(Animal animal, Person owner) {
        Animal animalWithOwner = createAnimalWithOwner(animal, owner);
        log.info("Save owner with id={}, to animal with id={}.", owner.getPersonId(), animal.getAnimalId());
        animalRepository.save(animalWithOwner);
        return AnimalConverter.convertAnimalToAnimalResponseDTO(animalWithOwner);
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
        log.info("Save death year={}, to animal with id={}.", deathYear, deathAnimal.getAnimalId());
        animalRepository.save(deathAnimal);
        return AnimalConverter.convertAnimalToAnimalResponseDTO(deathAnimal);
    }

    public AnimalResponseDTO removeOwnerFromAnimal(Animal animal) {
        if (animal.getOwner().isEmpty()) {
            log.info("Animal with id={}, is without owner.", animal.getAnimalId());
            return AnimalConverter.convertAnimalToAnimalResponseDTO(animal);
        } else {
            Animal animalWithoutOwner = createAnimalWithOwner(animal, null);
            log.info("Remove owner from animal with id={}.", animal.getAnimalId());
            animalRepository.save(animalWithoutOwner);
            return AnimalConverter.convertAnimalToAnimalResponseDTO(animalWithoutOwner);
        }
    }

    public void deleteAnimalById(Long animalId) {
        log.info("Remove animal with id={}, from database.", animalId);
        animalRepository.deleteById(animalId);
    }

    public boolean checkIfAnimalWithIdExist(Long animalId) {
        log.info("Check if animal with id={} exist.", animalId);
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
