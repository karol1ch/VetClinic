package com.chomoncik.clinic.model;

import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import lombok.Builder;


import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "animal")
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private final Long animalId;

    @Column(name = "animal_name")
    private final String animalName;

    private final String species;

    @Column(name = "birth_year")
    private final int birthYear;

    @Column(name = "death_year")
    private final int deathYear;

    @ManyToOne(fetch = FetchType.LAZY)
    private final Person owner;

    public Optional<Person> getOwner() {
        return Optional.ofNullable(this.owner);
    }

    public Animal() {
        this.animalId = 0L;
        this.animalName = null;
        this.species = null;
        this.birthYear = 0;
        this.deathYear = 0;
        this.owner = null;
    }

    public Animal(AnimalRequestDTO animalRequestDTO) {
        this.animalId = 0L;
        this.animalName = animalRequestDTO.getName();
        this.species = animalRequestDTO.getSpecies();
        this.birthYear = animalRequestDTO.getBirthYear();
        this.deathYear = animalRequestDTO.getDeathYear();
        this.owner = null;
    }

}
