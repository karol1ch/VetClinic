package com.chomoncik.clinic.model;

import com.chomoncik.clinic.model.DTO.AnimalRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import lombok.Builder;


import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

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

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private final Set<Appointment> appointmentSet;

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
        this.appointmentSet = null;
    }

    public Animal(AnimalRequestDTO animalRequestDTO) {
        this.animalId = 0L;
        this.animalName = animalRequestDTO.getName();
        this.species = animalRequestDTO.getSpecies();
        this.birthYear = animalRequestDTO.getBirthYear();
        this.deathYear = animalRequestDTO.getDeathYear();
        this.owner = null;
        this.appointmentSet = null;
    }

}
