package com.chomoncik.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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
        this.patient = null;
        this.appointmentTime = null;
        this.appointmentStatus = null;
        this.description = null;
        this.appointmentDateTime = null;
    }
}
