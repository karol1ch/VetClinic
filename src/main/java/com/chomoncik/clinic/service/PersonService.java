package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.PersonRepository;
import com.chomoncik.clinic.util.PersonUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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

    public PersonResponseDTO getPersonResponseDTOById(Long personId) {
        Person person = getPersonById(personId);
        return PersonUtils.convertPersonToPersonResponseDTO(person);
    }

    public List<PersonResponseDTO> getAllPeople() {
        List<Person> personList = personRepository.findAll();
        if (personList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Persons list is empty.");
        } else {
            return personList.stream()
                    .map(PersonUtils::convertPersonToPersonResponseDTO)
                    .collect(Collectors.toList());
        }
    }
}
