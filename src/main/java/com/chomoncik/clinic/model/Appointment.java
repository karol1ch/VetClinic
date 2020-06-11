package com.chomoncik.clinic.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Column(name = "appointment_date")
    private final LocalDate appointmentDate;

    @Column(name = "appointment_hour")
    private final LocalTime appointmentHour;

    @ManyToOne(fetch = FetchType.EAGER)
    private final Animal patient;

    public Appointment() {
        this.appointmentId = 0L;
        this.appointmentTime = null;
        this.appointmentStatus = null;
        this.description = null;
        this.appointmentDate = null;
        this.appointmentHour = null;
        this.patient = null;
    }

    public Appointment(LocalDateTime appointmentDateTime, AppointmentTime appointmentTime, Animal patient) {
        this.appointmentId = 0L;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = AppointmentStatus.ARRANGED;
        this.description = null;
        this.appointmentDate = appointmentDateTime.toLocalDate();
        this.appointmentHour = appointmentDateTime.toLocalTime();
        this.patient = patient;
    }
}
