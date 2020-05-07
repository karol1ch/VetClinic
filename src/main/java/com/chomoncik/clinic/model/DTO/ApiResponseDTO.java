package com.chomoncik.clinic.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDTO {
    private Boolean success;
    private String message;

}