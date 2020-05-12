package com.chomoncik.clinic.service;

import com.chomoncik.clinic.exception.ResourceNotFoundException;
import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Person addPerson(PersonRequestDTO personRequestDTO) {
        Person person = new Person(personRequestDTO);
        return personRepository.save(person);
    }

    public Person getPersonById(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Person with id " + personId + "not found.")
                );
    }
}
