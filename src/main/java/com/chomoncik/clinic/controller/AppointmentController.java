package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.service.AnimalService;
import com.chomoncik.clinic.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<?> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        if(!appointmentService.checkIfDateIsAtLeastTomorrow(appointmentRequestDTO.getAppointmentDateTime())) {
            log.error("Given appointment date {} is not after today.", appointmentRequestDTO.getAppointmentDateTime());
            return new ResponseEntity<>("Given appointment date " +
                    appointmentRequestDTO.getAppointmentDateTime() + " is not after today.", HttpStatus.BAD_REQUEST);
        }
        Optional<Animal> patient = animalService.getAnimalById(appointmentRequestDTO.getPatientId());
        if (patient.isEmpty()) {
            log.error("Animal with id={} not found.", appointmentRequestDTO .getPatientId());
            return new ResponseEntity<>("Animal with id " +
                    appointmentRequestDTO.getPatientId() + " not found.", HttpStatus.NOT_FOUND);
        }
        Appointment appointment = appointmentService.addAppointment(appointmentRequestDTO, patient.get());
        log.info("Create appointment with id={} for animal with id={}.",
                appointment.getAppointmentId(), appointment.getPatient().getAnimalId());
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

}

