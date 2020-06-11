package com.chomoncik.clinic.converter;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.dto.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;

import java.util.stream.Collectors;

public class PersonConverter {

    public static PersonResponseDTO convertPersonToPersonResponseDTO(Person person) {
        return PersonResponseDTO.builder()
                .name(person.getName())
                .surname(person.getSurname())
                .address(person.getAddress())
                .mail(person.getMail())
                .animalsSet(person.getAnimalSet().stream().map(Animal::getAnimalName).collect(Collectors.toUnmodifiableSet()))
                .build();
    }
}
