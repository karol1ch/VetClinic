package com.chomoncik.clinic.model.DTO;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class PersonResponseDTO {
    private String name;
    private String surname;
    private String address;
    private String contact;
    private Set<String> animalsSet;
}
