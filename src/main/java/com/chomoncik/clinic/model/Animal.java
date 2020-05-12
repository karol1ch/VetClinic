package com.chomoncik.clinic.model;

import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import lombok.Builder;
import org.springframework.stereotype.Controller;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Table(name = "animal")
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Animal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private final Long animalId;

    private final String name;

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
        this.name = null;
        this.species = null;
        this.birthYear = 0;
        this.deathYear = 0;
        this.owner = null;
    }

    public Animal(AnimalRequestDTO animalRequestDTO) {
        this.animalId = 0L;
        this.name = animalRequestDTO.getName();
        this.species = animalRequestDTO.getSpecies();
        this.birthYear = animalRequestDTO.getBirthYear();
        this.deathYear = animalRequestDTO.getDeathYear();
        this.owner = null;
    }

    @Builder
    public Animal(Person owner, int deathYear, Animal template) {
        this.animalId = template.animalId;
        this.name = template.getName();
        this.species = template.getSpecies();
        this.birthYear = template.getBirthYear();
        this.deathYear = deathYear == 0 ? template.getDeathYear() : deathYear;
        this.owner = owner == null ? template.getOwner().orElse(null) : owner;
    }
}
