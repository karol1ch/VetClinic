package com.chomoncik.clinic.service;

import com.chomoncik.clinic.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
}
