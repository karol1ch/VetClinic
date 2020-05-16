package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.service.AnimalService;
import com.chomoncik.clinic.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/animal")
public class AnimalController {

    private final AnimalService animalService;
    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Animal> addAnimal(@RequestBody AnimalRequestDTO animalRequestDTO) {
        return new ResponseEntity<>(animalService.addAnimal(animalRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "{animalId}")
    public ResponseEntity<?> getAnimalById(@PathVariable("animalId") Long animalId) {
        Optional<AnimalResponseDTO> animalResponseDTO = animalService.getAnimalResponseDTOById(animalId);
        if (animalResponseDTO.isEmpty()) {
            return new ResponseEntity<>("Animal with id " + animalId + " not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllAnimals() {
        List<AnimalResponseDTO> animalResponseDTOList = animalService.getAllAnimals();
        if (animalResponseDTOList.isEmpty()) {
            return new ResponseEntity<>("Animal list is empty.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(animalResponseDTOList, HttpStatus.OK);
    }

    @PatchMapping(path = "{animalId}/add_owner")
    public ResponseEntity<?> addOwnerToAnimal(@PathVariable("animalId") Long animalId,
                                              @RequestParam(value = "ownerId") Long ownerId) {
        Optional<Animal> animal = animalService.getAnimalById(animalId);
        if (animal.isEmpty()) {
            return new ResponseEntity<>("Animal with id " + animalId + " not found.", HttpStatus.NOT_FOUND);
        }
        Optional<Person> person = personService.getPersonById(ownerId);
        if (person.isEmpty()) {
            return new ResponseEntity<>("Person with id " + ownerId + " not found.", HttpStatus.NOT_FOUND);
        }
        AnimalResponseDTO animalResponseDTO = animalService.addOwner(animal.get(), person.get());
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "{animalId}")
    public ResponseEntity<?> addDeathYear(@PathVariable("animalId") Long animalId,
                                              @RequestParam(value = "deathYear") int deathYear) {
        Optional<Animal> animal = animalService.getAnimalById(animalId);
        if (animal.isEmpty()) {
            return new ResponseEntity<>("Animal with id " + animalId + " not found.", HttpStatus.NOT_FOUND);
        }
        AnimalResponseDTO animalResponseDTO = animalService.addDeathYear(animal.get(), deathYear);
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "{animalId}/remove_owner")
    public ResponseEntity<?> removeOwner(@PathVariable("animalId") Long animalId) {
        Optional<Animal> animal = animalService.getAnimalById(animalId);
        if (animal.isEmpty()) {
            return new ResponseEntity<>("Animal with id " + animalId + " not found.", HttpStatus.NOT_FOUND);
        }
        AnimalResponseDTO animalResponseDTO = animalService.removeOwnerFromAnimal(animal.get());
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping(path = "{animalId}")
    public ResponseEntity<?> deleteAnimalById(@PathVariable("animalId") Long animalId) {
        if (!animalService.checkIfAnimalWithIdExist(animalId)) {
            return new ResponseEntity<>("Animal with id " + animalId + " not found.", HttpStatus.NOT_FOUND);
        }
        animalService.deleteAnimalById(animalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
