package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.model.Person;
import com.chomoncik.clinic.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    private static final Long ID = 1L;
    private static final String ANIMAL_NAME = "tofik";
    private static final String SPECIES = "dog";
    private static final int BIRTH_YEAR = 2010;
    private static final int DEATH_YEAR = 2020;
    private static final String OWNER_NAME = "jack";
    private static final Person OWNER = Person.builder().name(OWNER_NAME).build();
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
    private static final AnimalResponseDTO ANIMAL_RESPONSE_DTO_WITHOUT_ANIMAL =
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

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void shouldGetAnimalById() {
        //GIVEN
        given(animalRepository.findById(ID)).willReturn(Optional.of(ANIMAL_WITHOUT_OWNER));

        //WHEN
        Animal animal = animalService.getAnimalById(ID).get();

        //THEN
        assertThat(animal).isEqualTo(ANIMAL_WITHOUT_OWNER);
    }

    @Test
    void shouldGetAnimalResponseDTOById() {
        //GIVEN
        given(animalRepository.findById(ID)).willReturn(Optional.of(ANIMAL_WITH_OWNER));

        //WHEN
        AnimalResponseDTO animalResponseDTO = animalService.getAnimalResponseDTOById(ID).get();

        //THEN
        assertThat(animalResponseDTO).isEqualTo(ANIMAL_RESPONSE_DTO);
    }

    @Test
    void shouldAddAnimal() {
        //GIVEN
        given(animalRepository.save(any(Animal.class))).willReturn(ANIMAL_WITHOUT_OWNER);

        //WHEN
        Animal animal = animalService.addAnimal(ANIMAL_REQUEST_DTO);

        //THEN
        assertThat(animal).isEqualTo(ANIMAL_WITHOUT_OWNER);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldGetAllAnimal() {
        //GIVEN
        given(animalRepository.findAll()).willReturn(List.of(ANIMAL_WITH_OWNER));

        //WHEN
        List<AnimalResponseDTO> animalResponseDTOList = animalService.getAllAnimals();

        //THEN
        animalResponseDTOList.forEach(x -> assertThat(x).isEqualTo(ANIMAL_RESPONSE_DTO));
        assertThat(animalResponseDTOList.size()).isEqualTo(1);
    }

    @Test
    void shouldAddOwner() {
        //GIVEN
        given(animalRepository.save(any(Animal.class))).willReturn(ANIMAL_WITH_OWNER);

        //WHEN
        AnimalResponseDTO animalResponseDTO = animalService.addOwner(ANIMAL_WITHOUT_OWNER, OWNER);

        //THEN
        assertThat(animalResponseDTO).isEqualTo(ANIMAL_RESPONSE_DTO);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldAddDeathYear() {
        //GIVEN
        given(animalRepository.save(any(Animal.class))).willReturn(ANIMAL_WITH_OWNER);

        //WHEN
        AnimalResponseDTO animalResponseDTO = animalService.addDeathYear(NOT_DEAD_ANIMAL, DEATH_YEAR);

        //THEN
        assertThat(animalResponseDTO).isEqualTo(ANIMAL_RESPONSE_DTO);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldRemoveOwnerFromAnimal() {
        //GIVEN
        given(animalRepository.save(any(Animal.class))).willReturn(ANIMAL_WITHOUT_OWNER);

        //WHEN
        AnimalResponseDTO animalResponseDTO = animalService.removeOwnerFromAnimal(ANIMAL_WITH_OWNER);

        //THEN
        assertThat(animalResponseDTO).isEqualTo(ANIMAL_RESPONSE_DTO_WITHOUT_ANIMAL);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldNotRemoveOwnerFromAnimalWhenOwnerNotExist() {
        //WHEN
        AnimalResponseDTO animalResponseDTO = animalService.removeOwnerFromAnimal(ANIMAL_WITHOUT_OWNER);

        //THEN
        assertThat(animalResponseDTO).isEqualTo(ANIMAL_RESPONSE_DTO_WITHOUT_ANIMAL);
    }

    @Test
    void shouldReturnTrueIfAnimalWithIdExist() {
        //GIVEN
        given(animalRepository.existsById(ID)).willReturn(Boolean.TRUE);

        //WHEN
        Boolean ifExist = animalService.checkIfAnimalWithIdExist(ID);

        //THEN
        assertThat(ifExist).isEqualTo(Boolean.TRUE);
        verify(animalRepository, times(1)).existsById(ID);
    }

    @Test
    void shouldDeleteAnimal() {
        //GIVEN
        AnimalRepository specialAnimalRepository = mock(AnimalRepository.class);
        AnimalService specialAnimalService = new AnimalService(specialAnimalRepository);

        //WHEN
        specialAnimalService.deleteAnimalById(ID);

        //THEN
        verify(specialAnimalRepository, times(1)).deleteById(ID);
    }

}