package com.chomoncik.clinic.converter;

import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.dto.AppointmentResponseDTO;

public class AppointmentConverter {

    public static AppointmentResponseDTO convertAppointmentToAppointmentResponseDTO(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .appointmentStatus(appointment.getAppointmentStatus().name())
                .appointmentDescription(appointment.getDescription())
                .patientId(appointment.getPatient().getAnimalId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentStartTime(appointment.getAppointmentHour())
                .appointmentEndTime(appointment.getAppointmentHour().plusMinutes(
                        appointment.getAppointmentTime().getValue()))
                .build();
    }
}
