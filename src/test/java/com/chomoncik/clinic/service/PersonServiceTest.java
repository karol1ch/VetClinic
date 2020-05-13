package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;


}