package com.chomoncik.clinic.util;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;

import java.util.stream.Collectors;

public class PersonUtils {

    public static PersonResponseDTO convertPersonToPersonResponseDTO(Person person) {
        return PersonResponseDTO.builder()
                .name(person.getName())
                .surname(person.getSurname())
                .address(person.getAddress())
                .contact(person.getContact())
                .animalsSet(person.getAnimalSet().stream().map(Animal::getAnimalName).collect(Collectors.toUnmodifiableSet()))
                .build();
    }
}
