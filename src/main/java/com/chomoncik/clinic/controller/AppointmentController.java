package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        return new ResponseEntity<>("Message", HttpStatus.OK);
    }

}

