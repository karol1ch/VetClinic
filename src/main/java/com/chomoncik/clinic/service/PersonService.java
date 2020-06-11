package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.dto.PersonRequestDTO;
import com.chomoncik.clinic.model.dto.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.PersonRepository;
import com.chomoncik.clinic.converter.PersonConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    public Person addPerson(PersonRequestDTO personRequestDTO) {
        Person createdPerson = personRepository.save(new Person(personRequestDTO));
        log.info("Save person with id={}.", createdPerson.getPersonId());
        return createdPerson;
    }

    public Optional<Person> getPersonById(Long personId) {
        log.info("Get person with id={}.", personId);
        return personRepository.findById(personId);
    }

    public Optional<PersonResponseDTO> getPersonResponseDTOById(Long personId) {
        Optional<Person> person = getPersonById(personId);
        return person.map(PersonConverter::convertPersonToPersonResponseDTO);
    }

    public List<PersonResponseDTO> getAllPeople() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonConverter::convertPersonToPersonResponseDTO)
                .collect(Collectors.toList());
    }
}
