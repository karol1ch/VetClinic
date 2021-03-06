package com.chomoncik.clinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnimalResponseDTO {
    private Long animalId;
    private String name;
    private String species;
    private int birthYear;
    private int deathYear;
    private String owner;
}
