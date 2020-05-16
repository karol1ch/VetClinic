package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Appointment addAppointment(AppointmentRequestDTO appointmentRequestDTO, Animal animal) {
        Appointment appointment = new Appointment(
                appointmentRequestDTO.getAppointmentDateTime(),
                appointmentRequestDTO.getAppointmentTime(),
                animal);
        return appointmentRepository.save(appointment);
    }
}
