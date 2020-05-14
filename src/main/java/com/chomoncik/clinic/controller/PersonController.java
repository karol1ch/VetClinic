package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/person")
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody PersonRequestDTO personRequestDTO) {
        return new ResponseEntity<>(personService.addPerson(personRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable("personId") Long personId) {
        Optional<PersonResponseDTO> personResponseDTO = personService.getPersonResponseDTOById(personId);
        if (personResponseDTO.isEmpty()) {
            return new ResponseEntity<>("Person with id " + personId + " not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(personResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPeople() {
        List<PersonResponseDTO> personResponseDTOList = personService.getAllPeople();
        if (personResponseDTOList.isEmpty()) {
            return new ResponseEntity<>("Persons list is empty.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(personResponseDTOList, HttpStatus.OK);

    }
}
