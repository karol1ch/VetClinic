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
                                "Animal with id " + animalId + "not found.")
                );
    }

    public AnimalResponseDTO addOwner(Long animalId, Long ownerId) {
        Animal animal = getAnimalById(animalId);
        Person owner = personService.getPersonById(ownerId);
        Animal animalWithOwner = Animal.builder().template(animal).owner(owner).build();
        animalRepository.save(animalWithOwner);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(animalWithOwner);
    }

    public AnimalResponseDTO addDeathYear(Long animalId, int deathYear) {
        Animal deathAnimal = Animal.builder().template(getAnimalById(animalId)).deathYear(deathYear).build();
        animalRepository.save(deathAnimal);
        return AnimalUtils.convertAnimalToAnimalResponseDTO(deathAnimal);
    }
}
