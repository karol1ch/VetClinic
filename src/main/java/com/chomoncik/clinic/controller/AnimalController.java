package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/animal")
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<?> addAnimal(@RequestBody AnimalRequestDTO animalRequestDTO) {
        return new ResponseEntity<>(animalService.addAnimal(animalRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "{animalId}")
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable("animalId") Long animalId) {
        AnimalResponseDTO animalResponseDTO = animalService.getAnimalResponseDTOById(animalId);
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "{animalId}/add_owner")
    public ResponseEntity<?> addOwnerToAnimal(@PathVariable("animalId") Long animalId,
                                              @RequestParam(value = "ownerId") Long ownerId) {
        AnimalResponseDTO animalResponseDTO = animalService.addOwner(animalId, ownerId);
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "{animalId}")
    public ResponseEntity<?> addDeathYear(@PathVariable("animalId") Long animalId,
                                              @RequestParam(value = "deathYear") int deathYear) {
        AnimalResponseDTO animalResponseDTO = animalService.addDeathYear(animalId, deathYear);
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }
}
