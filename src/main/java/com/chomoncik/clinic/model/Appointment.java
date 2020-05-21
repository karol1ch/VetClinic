package com.chomoncik.clinic.model;

import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private final Long appointmentId;

    @Enumerated
    private final AppointmentTime appointmentTime;

    @Enumerated
    private final AppointmentStatus appointmentStatus;

    private final String description;

    @Column(name = "appointment_date_time")
    private final LocalDateTime appointmentDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private final Animal patient;

    public Appointment() {
        this.appointmentId = 0L;
        this.appointmentTime = null;
        this.appointmentStatus = null;
        this.description = null;
        this.appointmentDateTime = null;
        this.patient = null;
    }

    public Appointment(LocalDateTime appointmentDateTime, AppointmentTime appointmentTime, Animal patient) {
        this.appointmentId = 0L;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = AppointmentStatus.ARRANGED;
        this.description = null;
        this.appointmentDateTime = appointmentDateTime;
        this.patient = patient;
    }
}
