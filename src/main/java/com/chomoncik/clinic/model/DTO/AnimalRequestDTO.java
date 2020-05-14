package com.chomoncik.clinic.model.DTO;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AnimalRequestDTO {
    private String name;
    private String species;
    private int birthYear;
    private int deathYear;
}
