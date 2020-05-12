package com.chomoncik.clinic.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class PersonResponseDTO {
    private String name;
    private String surname;
    private String address;
    private String contact;
    private Set<String> animalsSet;
}
