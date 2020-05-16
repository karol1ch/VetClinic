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

@RestController
@AllArgsConstructor
@RequestMapping(path = "/appointment")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<?> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        Optional<Animal> patient = animalService.getAnimalById(appointmentRequestDTO.getPatientId());
        if (patient.isEmpty()) {
            return new ResponseEntity<>("Animal with id " + patient + " not found.", HttpStatus.NOT_FOUND);
        }
        Appointment appointment = appointmentService.addAppointment(appointmentRequestDTO, patient.get());
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

}

