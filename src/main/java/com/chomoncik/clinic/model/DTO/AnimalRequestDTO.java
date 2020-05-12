package com.chomoncik.clinic.model.DTO;

import com.chomoncik.clinic.model.Person;
import lombok.Getter;

@Getter
public class AnimalRequestDTO {
    private String name;
    private String species;
    private int birthYear;
    private int deathYear;
}
