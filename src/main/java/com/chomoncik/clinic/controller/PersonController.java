package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<PersonResponseDTO> getPersonById(@PathVariable("personId") Long personId) {
        PersonResponseDTO personResponseDTO = personService.getPersonResponseDTOById(personId);
        return new ResponseEntity<>(personResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getAllPeople() {
        List<PersonResponseDTO> personResponseDTOList = personService.getAllPeople();
        return new ResponseEntity<>(personResponseDTOList, HttpStatus.OK);

    }
}
