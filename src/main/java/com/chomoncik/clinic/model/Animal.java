package com.chomoncik.clinic.model;

import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "animal")
@Getter
@AllArgsConstructor
public class Animal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
