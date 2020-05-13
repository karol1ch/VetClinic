package com.chomoncik.clinic.util;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.DTO.AnimalResponseDTO;
import com.chomoncik.clinic.model.Person;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AnimalUtilsTest {

    private static final Long ID = 1L;
    private static final String ANIMAL_NAME = "tofik";
    private static final String SPECIES = "dog";
    private static final int BIRTH_YEAR = 2010;
    private static final int DEATH_YEAR = 2020;
    private static final Person OWNER = Person.builder().name("jack").build();

    @Test
    void shouldConvertAnimalToAnimalResponseDTOWhenOwnerExistAndAnimalIsDead() {
        //GIVEN
        Animal animal = Animal.builder()
                .animalId(ID)
                .animalName(ANIMAL_NAME)
                .species(SPECIES)
                .birthYear(BIRTH_YEAR)
                .deathYear(DEATH_YEAR)
                .owner(OWNER)
                .build();

        //WHEN
        AnimalResponseDTO animalResponseDTO = AnimalUtils.convertAnimalToAnimalResponseDTO(animal);

        //THEN
        assertThat(animalResponseDTO.getAnimalId()).isEqualTo(ID);
        assertThat(animalResponseDTO.getName()).isEqualTo(ANIMAL_NAME);
        assertThat(animalResponseDTO.getSpecies()).isEqualTo(SPECIES);
        assertThat(animalResponseDTO.getBirthYear()).isEqualTo(BIRTH_YEAR);
        assertThat(animalResponseDTO.getDeathYear()).isEqualTo(DEATH_YEAR);
        assertThat(animalResponseDTO.getOwner()).isEqualTo(OWNER.getName());


    }

    @Test
    void shouldConvertAnimalToAnimalResponseDTOWhenOwnerNotExistAndAnimalIsAlive() {
        //GIVEN
        Animal animal = Animal.builder()
                .animalId(ID)
                .animalName(ANIMAL_NAME)
                .species(SPECIES)
                .birthYear(BIRTH_YEAR)
                .build();

        //WHEN
        AnimalResponseDTO animalResponseDTO = AnimalUtils.convertAnimalToAnimalResponseDTO(animal);

        //THEN
        AssertionsForClassTypes.assertThat(animalResponseDTO.getAnimalId()).isEqualTo(ID);
        AssertionsForClassTypes.assertThat(animalResponseDTO.getName()).isEqualTo(ANIMAL_NAME);
        AssertionsForClassTypes.assertThat(animalResponseDTO.getSpecies()).isEqualTo(SPECIES);
        AssertionsForClassTypes.assertThat(animalResponseDTO.getBirthYear()).isEqualTo(BIRTH_YEAR);
        AssertionsForClassTypes.assertThat(animalResponseDTO.getDeathYear()).isEqualTo(0);
        AssertionsForClassTypes.assertThat(animalResponseDTO.getOwner()).isEqualTo(null);
    }

}