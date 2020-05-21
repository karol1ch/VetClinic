package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final Clock clock;

    public Appointment addAppointment(AppointmentRequestDTO appointmentRequestDTO, Animal animal) {
        Appointment createdAppointment = appointmentRepository.save(
                new Appointment(appointmentRequestDTO.getAppointmentDateTime(),
                                appointmentRequestDTO.getAppointmentTime(),
                                animal
                )
        );
        log.info("Save appointment with id={}.", createdAppointment.getAppointmentId());
        return createdAppointment;
    }

    public boolean checkIfDateIsAtLeastTomorrow(LocalDateTime requestedDateTime) {
        log.info("Checking if date {}, is valid.", requestedDateTime);
        return requestedDateTime.toLocalDate().isAfter(LocalDate.now(clock));
    }
}
