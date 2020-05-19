package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.PersonRepository;
import com.chomoncik.clinic.util.PersonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    public Person addPerson(PersonRequestDTO personRequestDTO) {
        Person person = new Person(personRequestDTO);
        log.info("Save person with id={}.", person.getPersonId());
        return personRepository.save(person);
    }

    public Optional<Person> getPersonById(Long personId) {
        return personRepository.findById(personId);
    }

    public Optional<PersonResponseDTO> getPersonResponseDTOById(Long personId) {
        Optional<Person> person = getPersonById(personId);
        return person.map(PersonUtils::convertPersonToPersonResponseDTO);
    }

    public List<PersonResponseDTO> getAllPeople() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonUtils::convertPersonToPersonResponseDTO)
                .collect(Collectors.toList());
    }
}
