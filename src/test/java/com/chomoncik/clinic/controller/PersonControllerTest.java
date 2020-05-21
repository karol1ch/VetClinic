package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import com.chomoncik.clinic.model.DTO.PersonResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

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
            .contact(CONTACT)
            .build();

    private static final Person PERSON_WITH_ANIMALS = Person.builder()
            .personId(ID)
            .name(NAME)
            .surname(SURNAME)
            .address(ADDRESS)
            .contact(CONTACT)
            .animalSet(ANIMAL_SET)
            .build();

    private static final PersonRequestDTO PERSON_REQUEST_DTO = PersonRequestDTO.builder()
            .name(NAME)
            .surname(SURNAME)
            .address(ADDRESS)
            .contact(CONTACT)
            .build();

    private static final PersonResponseDTO PERSON_RESPONSE_DTO = PersonResponseDTO.builder()
            .name(NAME)
            .surname(SURNAME)
            .address(ADDRESS)
            .contact(CONTACT)
            .animalsSet(ANIMAL_NAMES)
            .build();



    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    private void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .build();
    }

    @Test
    void shouldGetPersonResponseDTOById() throws Exception {
        //GIVEN
        given(personService.getPersonResponseDTOById(1L)).willReturn(Optional.of(PERSON_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/person/{personId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(NAME);
    }

    @Test
    void shouldReturnNotFoundExceptionWhenPersonNotExist() throws Exception {
        //GIVEN
        given(personService.getPersonResponseDTOById(1L)).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc. perform(
                get("/person/{personId}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Person with id 1 not found.");
    }

    @Test
    void shouldAddPerson() throws Exception {
        //GIVEN
        given(personService.addPerson(any())).willReturn(PERSON_WITHOUT_ANIMALS);
        ObjectMapper mapper = new ObjectMapper();

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                post("/person")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(PERSON_REQUEST_DTO))
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains(NAME);
        assertThat(response.getContentAsString()).contains(SURNAME);
    }

    @Test
    void shouldGetAllPeople() throws Exception {
        //GIVEN
        given(personService.getAllPeople()).willReturn(List.of(PERSON_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/person")
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(NAME);
    }

    @Test
    void shouldReturnNotFoundExceptionWhenPeopleListIsEmpty() throws Exception {
        //GIVEN
        given(personService.getAllPeople()).willReturn(Lists.emptyList());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/person")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("People list is empty.");
    }

}