package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.model.DTO.ApiResponseDTO;
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

    @GetMapping(path = "{id}")
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable("id") Long id) {
        AnimalResponseDTO animalResponseDTO = animalService.getAnimalResponseDTOById(id);
        return new ResponseEntity<>(animalResponseDTO, HttpStatus.OK);
    }

//    @PatchMapping(path = "{id}/add_owner")
//    public ResponseEntity<?> addOwnerToAnimal(@RequestParam)
}
