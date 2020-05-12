package com.chomoncik.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "appointment")
@Getter
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private final Long animalId;

    public Appointment() {
        this.animalId = 0L;
    }
}
