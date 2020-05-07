package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/person")
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody PersonRequestDTO personRequestDTO) {
        return new ResponseEntity<>(personService.addPerson(personRequestDTO), HttpStatus.CREATED);
    }
}
