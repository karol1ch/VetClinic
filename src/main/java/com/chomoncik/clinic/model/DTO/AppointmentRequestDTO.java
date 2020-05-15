package com.chomoncik.clinic.model.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AppointmentRequestDTO {
    private LocalDateTime appointmentDateTime;
    private Long patientId;
}
