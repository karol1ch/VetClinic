package com.chomoncik.clinic.model.dto;

import com.chomoncik.clinic.model.AppointmentTime;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AppointmentRequestDTO {
    private LocalDateTime appointmentDateTime;
    private AppointmentTime appointmentTime;
    private Long patientId;
}
