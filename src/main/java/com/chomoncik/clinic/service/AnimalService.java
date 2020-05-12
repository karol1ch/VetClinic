package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.AnimalRepository;
import com.chomoncik.clinic.util.AnimalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final PersonService personService;

    public Animal addAnimal(AnimalRequestDTO animalRequestDTO) {
        Animal animal = new Animal(animalRequestDTO);
        return animalRepository.save(animal);
    }

    public AnimalResponseDTO getAnimalResponseDTOById(Long animalId) {
        Animal animal = getAnimalById(animalId);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(animal);
    }

    public Animal getAnimalById(Long animalId) {
        return animalRepository.findById(animalId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Animal with id " + animalId + " not found.")
                );
    }

    public List<AnimalResponseDTO> getAllAnimals() {
        List<Animal> animalList = animalRepository.findAll();
        if (animalList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Animals list is empty.");
        } else {
            return animalList.stream()
                    .map(AnimalUtils::convertAnimalToAnimalResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    public AnimalResponseDTO addOwner(Long animalId, Long ownerId) {
        Animal animal = getAnimalById(animalId);
        Person owner = personService.getPersonById(ownerId);
        Animal animalWithOwner = Animal.builder()
                .animalId(animal.getAnimalId())
                .name(animal.getName())
                .species(animal.getSpecies())
                .birthYear(animal.getBirthYear())
                .deathYear(animal.getDeathYear())
                .owner(owner)
                .build();
        animalRepository.save(animalWithOwner);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(animalWithOwner);
    }

    public AnimalResponseDTO addDeathYear(Long animalId, int deathYear) {
        Animal animal = getAnimalById(animalId);
        Animal deathAnimal = Animal.builder()
                .animalId(animal.getAnimalId())
                .name(animal.getName())
                .species(animal.getSpecies())
                .birthYear(animal.getBirthYear())
                .deathYear(deathYear)
                .owner(animal.getOwner().orElse(null))
                .build();
        animalRepository.save(deathAnimal);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(deathAnimal);
    }

    public AnimalResponseDTO removeOwnerFromAnimal(Long animalId) {
        Animal animal = getAnimalById(animalId);
        if (animal.getOwner().isEmpty()) {
            return AnimalUtils.convertAnimalToAnimalResponseDTO(animal);
        } else {
            Animal animalWithoutOwner = Animal.builder()
                    .animalId(animal.getAnimalId())
                    .name(animal.getName())
                    .species(animal.getSpecies())
                    .birthYear(animal.getBirthYear())
                    .deathYear(animal.getDeathYear())
                    .owner(null)
                    .build();
            animalRepository.save(animalWithoutOwner);
            return AnimalUtils.convertAnimalToAnimalResponseDTO(animalWithoutOwner);
        }
    }

    public void deleteAnimalById(Long animalId) {
        if (animalRepository.existsById(animalId)) {
            animalRepository.deleteById(animalId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Animal with id " + animalId + " not found.");
        }
    }
}
