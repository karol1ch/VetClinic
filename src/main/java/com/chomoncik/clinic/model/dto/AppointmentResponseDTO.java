package com.chomoncik.clinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class AppointmentResponseDTO {
    private Long appointmentId;
    private String appointmentStatus;
    private String appointmentDescription;
    private Long patientId;
    private LocalDate appointmentDate;
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;
}

