package com.chomoncik.clinic.util;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PersonUtilsTest {

    private static final Long ID = 1L;
    private static final String NAME = "jack";
    private static final String SURNAME = "walker";
    private static final String ADDRESS = "Street 1";
    private static final String CONTACT = "email@gmail.com and 123";
    private static final Set<Animal> ANIMAL_SET = new HashSet<>(Arrays.asList(
            Animal.builder().animalId(1L).animalName("burek").build(),
            Animal.builder().animalId(2L).animalName("tofik").build()
    ));
    private static final Set<String> ANIMAL_NAMES = new HashSet<>(Arrays.asList("burek", "tofik"));


    @Test
    void shouldConvertPersonToPersonResponseDTO() {
        //GIVEN
        Person person = Person.builder()
                .personId(ID)
                .name(NAME)
                .surname(SURNAME)
                .address(ADDRESS)
                .mail(CONTACT)
                .animalSet(ANIMAL_SET)
                .build();

        //WHEN
        PersonResponseDTO personResponseDTO = PersonUtils.convertPersonToPersonResponseDTO(person);

        //THEN
        assertThat(personResponseDTO.getName()).isEqualTo(NAME);
        assertThat(personResponseDTO.getSurname()).isEqualTo(SURNAME);
        assertThat(personResponseDTO.getAddress()).isEqualTo(ADDRESS);
        assertThat(personResponseDTO.getMail()).isEqualTo(CONTACT);
        assertThat(personResponseDTO.getAnimalsSet()).isEqualTo(ANIMAL_NAMES);
    }
}