package com.chomoncik.clinic.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDTO {
    private String name;
    private String surname;
    private String address;
    private String mail;
}
