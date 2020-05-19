package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Appointment addAppointment(AppointmentRequestDTO appointmentRequestDTO, Animal animal) {
        Appointment appointment = new Appointment(
                appointmentRequestDTO.getAppointmentDateTime(),
                appointmentRequestDTO.getAppointmentTime(),
                animal);
        log.info("Save appointment with id={}.", appointment.getAppointmentId());
        return appointmentRepository.save(appointment);
    }

    public boolean checkIfDateIsAtLeastTomorrow(LocalDateTime requestedDateTime) {
        return requestedDateTime.toLocalDate().isAfter(LocalDate.now());
    }
}
