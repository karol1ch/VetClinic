package com.chomoncik.clinic.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AppointmentInfoRequestDTO {
    boolean isCompleted;
    String description;
}
