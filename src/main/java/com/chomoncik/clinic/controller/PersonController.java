package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/person")
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody PersonRequestDTO personRequestDTO) {
        Person person = personService.addPerson(personRequestDTO);
        log.info("Create person with id={}.", person.getPersonId());
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @GetMapping(path = "{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable("personId") Long personId) {
        Optional<PersonResponseDTO> personResponseDTO = personService.getPersonResponseDTOById(personId);
        if (personResponseDTO.isEmpty()) {
            log.error("Person with id={} not found.", personId);
            return new ResponseEntity<>("Person with id " + personId + " not found.", HttpStatus.NOT_FOUND);
        }
        log.info("Return person with id={}.", personId);
        return new ResponseEntity<>(personResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPeople() {
        List<PersonResponseDTO> personResponseDTOList = personService.getAllPeople();
        if (personResponseDTOList.isEmpty()) {
            log.error("People list is empty.");
            return new ResponseEntity<>("People list is empty.", HttpStatus.NOT_FOUND);
        }
        log.info("Return people list.");
        return new ResponseEntity<>(personResponseDTOList, HttpStatus.OK);
    }
}
