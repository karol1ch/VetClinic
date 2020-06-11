package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.dto.PersonRequestDTO;
import com.chomoncik.clinic.model.dto.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
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
    private static final Person PERSON_WITHOUT_ANIMALS = Person.builder()
                                                                .personId(ID)
                                                                .name(NAME)
                                                                .surname(SURNAME)
                                                                .address(ADDRESS)
                                                                .mail(CONTACT)
                                                                .build();

    private static final Person PERSON_WITH_ANIMALS = Person.builder()
                                                                .personId(ID)
                                                                .name(NAME)
                                                                .surname(SURNAME)
                                                                .address(ADDRESS)
                                                                .mail(CONTACT)
                                                                .animalSet(ANIMAL_SET)
                                                                .build();

    private static final PersonRequestDTO PERSON_REQUEST_DTO = PersonRequestDTO.builder()
                                                                .name(NAME)
                                                                .surname(SURNAME)
                                                                .address(ADDRESS)
                                                                .mail(CONTACT)
                                                                .build();

    private static final PersonResponseDTO PERSON_RESPONSE_DTO = PersonResponseDTO.builder()
                                                                .name(NAME)
                                                                .surname(SURNAME)
                                                                .address(ADDRESS)
                                                                .mail(CONTACT)
                                                                .animalsSet(ANIMAL_NAMES)
                                                                .build();


    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void shouldGetPersonById() {
        //GIVEN
        given(personRepository.findById(ID)).willReturn(Optional.of(PERSON_WITH_ANIMALS));

        //WHEN
        Person person = personService.getPersonById(ID).get() ;

        //THEN
        assertThat(person).isEqualTo(PERSON_WITH_ANIMALS);
    }

    @Test
    void shouldAddPerson() {
        //GIVEN
        given(personRepository.save(any(Person.class))).willReturn(PERSON_WITHOUT_ANIMALS);

        //WHEN
        Person person = personService.addPerson(PERSON_REQUEST_DTO);

        //THEN
        assertThat(person).isEqualTo(PERSON_WITHOUT_ANIMALS);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void shouldGetPersonResponseDTOById() {
        //GIVEN
        given(personRepository.findById(ID)).willReturn(Optional.of(PERSON_WITH_ANIMALS));

        //WHEN
        PersonResponseDTO personResponseDTO = personService.getPersonResponseDTOById(ID).get();

        //THEN
        assertThat(personResponseDTO).isEqualTo(PERSON_RESPONSE_DTO);
    }

    @Test
    void shouldGetAllPeople() {
        //GIVEN
        given(personRepository.findAll()).willReturn(List.of(PERSON_WITH_ANIMALS));

        //WHEN
        List<PersonResponseDTO> personResponseDTOList = personService.getAllPeople();

        //THEN
        personResponseDTOList.forEach(x -> assertThat(x).isEqualTo(PERSON_RESPONSE_DTO));
        assertThat(personResponseDTOList.size()).isEqualTo(1);
    }


}