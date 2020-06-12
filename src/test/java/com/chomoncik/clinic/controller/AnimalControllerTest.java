package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.model.dto.AnimalRequestDTO;
import com.chomoncik.clinic.model.dto.AnimalResponseDTO;
import com.chomoncik.clinic.service.AnimalService;
import com.chomoncik.clinic.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.AssertionsForClassTypes;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class AnimalControllerTest {

    private static final Long ID = 1L;
    private static final Long OWNER_ID = 1L;
    private static final String ANIMAL_NAME = "tofik";
    private static final String SPECIES = "dog";
    private static final int BIRTH_YEAR = 2010;
    private static final int DEATH_YEAR = 2020;
    private static final String OWNER_NAME = "jack";
    private static final Person OWNER = Person.builder().personId(OWNER_ID).name(OWNER_NAME).build();
    private static final AnimalRequestDTO ANIMAL_REQUEST_DTO = AnimalRequestDTO.builder()
            .name(ANIMAL_NAME)
            .species(SPECIES)
            .birthYear(BIRTH_YEAR)
            .deathYear(DEATH_YEAR)
            .build();
    private static final AnimalResponseDTO ANIMAL_RESPONSE_DTO = AnimalResponseDTO.builder()
            .animalId(ID)
            .name(ANIMAL_NAME)
            .species(SPECIES)
            .birthYear(BIRTH_YEAR)
            .deathYear(DEATH_YEAR)
            .owner(OWNER_NAME)
            .build();
    private static final AnimalResponseDTO ANIMAL_RESPONSE_DTO_WITHOUT_OWNER =
            AnimalResponseDTO.builder()
                    .animalId(ID)
                    .name(ANIMAL_NAME)
                    .species(SPECIES)
                    .birthYear(BIRTH_YEAR)
                    .deathYear(DEATH_YEAR)
                    .build();
    private static final Animal ANIMAL_WITH_OWNER = Animal.builder()
            .animalId(ID)
            .animalName(ANIMAL_NAME)
            .species(SPECIES)
            .birthYear(BIRTH_YEAR)
            .deathYear(DEATH_YEAR)
            .owner(OWNER)
            .build();

    private static final Animal ANIMAL_WITHOUT_OWNER = Animal.builder()
            .animalId(ID)
            .animalName(ANIMAL_NAME)
            .species(SPECIES)
            .birthYear(BIRTH_YEAR)
            .deathYear(DEATH_YEAR)
            .build();

    private static final Animal NOT_DEAD_ANIMAL = Animal.builder()
            .animalId(ID)
            .animalName(ANIMAL_NAME)
            .species(SPECIES)
            .birthYear(BIRTH_YEAR)
            .owner(OWNER)
            .build();
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Mock
    AnimalService animalService;

    @Mock
    PersonService personService;

    @InjectMocks
    AnimalController animalController;

    @BeforeEach
    private void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(animalController)
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void shouldAddAnimal() throws Exception {
        //GIVEN
        given(animalService.addAnimal(any())).willReturn(ANIMAL_WITHOUT_OWNER);
        ObjectMapper mapper = new ObjectMapper();

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                post("/animal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ANIMAL_REQUEST_DTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        AssertionsForClassTypes.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        AssertionsForClassTypes.assertThat(response.getContentAsString()).contains(ANIMAL_NAME);
        AssertionsForClassTypes.assertThat(response.getContentAsString()).contains(SPECIES);
    }

    @Test
    void shouldGetAnimalResponseDTOById() throws Exception {
        //GIVEN
        given(animalService.getAnimalResponseDTOById(1L)).willReturn(Optional.of(ANIMAL_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/animal/{animalId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(ANIMAL_NAME);
    }

    @Test
    void shouldReturnNotFoundWhenAnimalNotExist() throws Exception {
        //GIVEN
        given(animalService.getAnimalResponseDTOById(1L)).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/animal/{animalId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id 1 not found.");
    }

    @Test
    void shouldGetAllAnimals() throws Exception {
        //GIVEN
        given(animalService.getAllAnimals()).willReturn(List.of(ANIMAL_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/animal")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(ANIMAL_NAME);
    }

    @Test
    void shouldReturnNotFoundWhenAnimalsListIsEmpty() throws Exception {
        //GIVEN
        given(animalService.getAllAnimals()).willReturn(Lists.emptyList());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/animal")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal list is empty.");
    }

    @Test
    void shouldAddDeathYearToAnimal() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.ofNullable(NOT_DEAD_ANIMAL));
        given(animalService.addDeathYear(any(Animal.class), anyInt())).willReturn(ANIMAL_RESPONSE_DTO);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}", 1L)
                        .param("deathYear", String.valueOf(DEATH_YEAR))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(String.valueOf(DEATH_YEAR));
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAnimalNotExistsDuringAddDeathYear() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}", 1L)
                        .param("deathYear", String.valueOf(DEATH_YEAR))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id 1 not found.");
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAnimalNotExistsDuringAddOwner() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}/add_owner", 1L)
                        .param("ownerId", String.valueOf(1))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id 1 not found.");
    }

    @Test
    void shouldReturnNotFoundExceptionWhenPersonNotExistsDuringAddOwner() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.ofNullable(ANIMAL_WITHOUT_OWNER));
        given(personService.getPersonById(anyLong())).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}/add_owner", 1L)
                        .param("ownerId", String.valueOf(1))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Person with id 1 not found.");
    }

    @Test
    void shouldAddOwnerToAnimal() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.ofNullable(ANIMAL_WITHOUT_OWNER));
        given(personService.getPersonById(anyLong())).willReturn(Optional.ofNullable(OWNER));
        given(animalService.addOwner(ANIMAL_WITHOUT_OWNER, OWNER)).willReturn(ANIMAL_RESPONSE_DTO);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}/add_owner", 1L)
                        .param("ownerId", String.valueOf(1))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(String.valueOf(OWNER_ID));
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAnimalNotExistsDuringRemoveOwner() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}/remove_owner", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id 1 not found.");
    }

    @Test
    void shouldRemoveOwnerFromAnimal() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.ofNullable(ANIMAL_WITH_OWNER));
        given(animalService.removeOwnerFromAnimal(ANIMAL_WITH_OWNER)).willReturn(ANIMAL_RESPONSE_DTO_WITHOUT_OWNER);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                patch("/animal/{animalId}/remove_owner", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"owner\":null");
    }

    @Test
    void shouldReturnNotFoundExceptionIfAnimalNotExistsDuringDeleteAnimal() throws Exception {
        //GIVEN
        given(animalService.checkIfAnimalWithIdExist(anyLong())).willReturn(true);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                delete("/animal/{animalId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldDeleteAnimal() throws Exception {
        //GIVEN
        given(animalService.checkIfAnimalWithIdExist(anyLong())).willReturn(false);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                delete("/animal/{animalId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id 1 not found.");
    }
}