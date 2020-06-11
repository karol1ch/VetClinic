package com.chomoncik.clinic.service;

import com.chomoncik.clinic.converter.AppointmentConverter;
import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.AppointmentStatus;
import com.chomoncik.clinic.model.dto.AppointmentInfoRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentResponseDTO;
import com.chomoncik.clinic.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        log.info("Get appointment with id={}.", appointmentId);
        return appointmentRepository.findById(appointmentId);
    }

    public Optional<AppointmentResponseDTO> getAppointmentResponseDTOById(Long appointmentId) {
        Optional<Appointment> appointment = getAppointmentById(appointmentId);
        return appointment.map(AppointmentConverter::convertAppointmentToAppointmentResponseDTO);
    }

    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointmentList = appointmentRepository.findAll();
        return appointmentList.stream()
                .map(AppointmentConverter::convertAppointmentToAppointmentResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAllAppointmentsByDate(LocalDate date) {
        List<Appointment> appointmentList = appointmentRepository.findAppointmentByAppointmentDate(date);
        return appointmentList.stream()
                .map(AppointmentConverter::convertAppointmentToAppointmentResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAllAppointmentByPatient(Animal animal) {
        List<Appointment> appointmentList = appointmentRepository.findAppointmentByPatient(animal);
        return appointmentList.stream()
                .map(AppointmentConverter::convertAppointmentToAppointmentResponseDTO)
                .collect(Collectors.toList());
    }

    public AppointmentResponseDTO addInfoAfterAppointment(Appointment appointment, AppointmentInfoRequestDTO appointmentInfoRequestDTO) {
        Appointment appointmentAfterFinished = createAppointmentAfterFinished(appointment, appointmentInfoRequestDTO);
        log.info("Save appointment with id={}.", appointmentAfterFinished.getAppointmentId());
        appointmentRepository.save(appointmentAfterFinished);
        return AppointmentConverter.convertAppointmentToAppointmentResponseDTO(appointmentAfterFinished);
    }

    public Optional<LocalDate> createLocalDateFromValues(int year, int month, int day) {
        try {
            return Optional.of(LocalDate.of(year, month, day));
        } catch (DateTimeException e) {
            log.error("Date with year={}, month={} and day={} not exist.", year, month, day);
            return Optional.empty();
        }
    }

    public boolean checkIfDateIsAtLeastTomorrow(LocalDateTime requestedDateTime) {
        log.info("Checking if date {}, is valid.", requestedDateTime);
        return requestedDateTime.toLocalDate().isAfter(LocalDate.now(clock));
    }

    public boolean checkIfAppointmentIsInWorkHours(LocalTime requestedTime, int appointmentTime) {
        log.info("Checking if time {}, is in work hours.", requestedTime);
        return requestedTime.isAfter(LocalTime.of(8, 0)) &&
                requestedTime.plusMinutes(appointmentTime).isBefore(LocalTime.of(20, 0, 1));
    }

    public boolean checkIfAppointmentTimeIsFree(LocalDateTime requestedDateTime, int appointmentTime) {
        log.info("Checking if date{}, is available for appointment.", requestedDateTime);
        List<AppointmentResponseDTO> appointmentList = getAllAppointmentsByDate(requestedDateTime.toLocalDate());
        LocalTime startTime = requestedDateTime.toLocalTime();
        LocalTime endTime = requestedDateTime.toLocalTime().plusMinutes(appointmentTime);
        return appointmentList.stream()
                .allMatch(appointment -> appointment.getAppointmentStartTime().isAfter(endTime) ||
                        appointment.getAppointmentEndTime().isBefore(startTime) ||
                        appointment.getAppointmentStartTime().equals(endTime) ||
                        appointment.getAppointmentEndTime().equals(startTime));
    }

    private Appointment createAppointmentAfterFinished(Appointment appointment, AppointmentInfoRequestDTO appointmentInfoRequestDTO) {
        return Appointment.builder()
                .appointmentId(appointment.getAppointmentId())
                .patient(appointment.getPatient())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentHour(appointment.getAppointmentHour())
                .appointmentStatus(appointmentInfoRequestDTO.isCompleted() ? AppointmentStatus.COMPLETED : AppointmentStatus.NOBODY_CAME)
                .description(appointmentInfoRequestDTO.getDescription())
                .build();
    }
}
