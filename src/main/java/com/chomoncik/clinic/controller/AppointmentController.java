package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.dto.AppointmentInfoRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentResponseDTO;
import com.chomoncik.clinic.service.AnimalService;
import com.chomoncik.clinic.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
        LocalDateTime appointmentDateTime = appointmentRequestDTO.getAppointmentDateTime();
        LocalTime appointmentHour = appointmentDateTime.toLocalTime();
        int appointmentDuration = appointmentRequestDTO.getAppointmentTime().getValue();

        if (!appointmentService.checkIfDateIsAtLeastTomorrow(appointmentDateTime)) {
            log.error("Given appointment date {} is not after today.", appointmentDateTime);
            return new ResponseEntity<>("Given appointment date " + appointmentDateTime +
                    " is not after today.", HttpStatus.BAD_REQUEST);
        }
        if (!appointmentService.checkIfAppointmentIsInWorkHours(appointmentHour, appointmentDuration)) {
            log.error("Given appointment time {} - {} is not in work hours.",
                    appointmentHour, appointmentHour.plusMinutes(appointmentDuration));
            return new ResponseEntity<>("Given appointment time: " + appointmentHour + " - " +
                    appointmentHour.plusMinutes(appointmentDuration) + " is not between 8AM and 8PM.",
                    HttpStatus.BAD_REQUEST);
        }
        if (!appointmentService.checkIfAppointmentTimeIsFree(appointmentDateTime, appointmentDuration)) {
            log.error("Given appointment datetime {} is not available.", appointmentDateTime);
            return new ResponseEntity<>("Given appointment datetime " + appointmentDateTime +
                    " is not available.", HttpStatus.CONFLICT);
        }
        Optional<Animal> patient = animalService.getAnimalById(appointmentRequestDTO.getPatientId());
        if (patient.isEmpty()) {
            log.error("Animal with id={} not found.", appointmentRequestDTO.getPatientId());
            return new ResponseEntity<>("Animal with id " +
                    appointmentRequestDTO.getPatientId() + " not found.", HttpStatus.NOT_FOUND);
        }
        Appointment appointment = appointmentService.addAppointment(appointmentRequestDTO, patient.get());
        log.info("Create appointment with id={} for animal with id={}.",
                appointment.getAppointmentId(), appointment.getPatient().getAnimalId());
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @GetMapping(path = "{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) {
        Optional<AppointmentResponseDTO> appointmentResponseDTO = appointmentService.getAppointmentResponseDTOById(appointmentId);
        if (appointmentResponseDTO.isEmpty()) {
            log.error("Appointment with id={} not found.", appointmentId);
            return new ResponseEntity<>("Appointment with id " + appointmentId + " not found.", HttpStatus.NOT_FOUND);
        }
        log.info("Return appointment with id={}", appointmentId);
        return new ResponseEntity<>(appointmentResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        List<AppointmentResponseDTO> appointmentResponseDTOList = appointmentService.getAllAppointments();
        if (appointmentResponseDTOList.isEmpty()) {
            log.error("Appointment list is empty.");
            return new ResponseEntity<>("Appointment list is empty.", HttpStatus.NOT_FOUND);
        }
        log.info("Return appointment list.");
        return new ResponseEntity<>(appointmentResponseDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/at_date")
    public ResponseEntity<?> getAppointmentsByDate(@RequestParam(value = "year") int year,
                                                   @RequestParam(value = "month") int month,
                                                   @RequestParam(value = "day") int day) {
        Optional<LocalDate> optionalDate = appointmentService.createLocalDateFromValues(year, month, day);
        if (optionalDate.isEmpty()) {
            return new ResponseEntity<>("Date not exist.", HttpStatus.BAD_REQUEST);
        }
        LocalDate date = optionalDate.get();
        List<AppointmentResponseDTO> appointmentResponseDTOList = appointmentService
                .getAllAppointmentsByDate(date);
        if (appointmentResponseDTOList.isEmpty()) {
            log.error("Any appointment with date={} not found.", date);
            return new ResponseEntity<>("Any appointment with date " + date + " not found.", HttpStatus.NOT_FOUND);
        }
        log.info("Return appointment list with date={}", date);
        return new ResponseEntity<>(appointmentResponseDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/patient/{animalId}")
    public ResponseEntity<?> getAppointmentsByPatientId(@PathVariable(value = "animalId") Long animalId) {
        Optional<Animal> patient = animalService.getAnimalById(animalId);
        if (patient.isEmpty()) {
            log.error("Animal with id={} not found.", animalId);
            return new ResponseEntity<>("Animal with id " +
                    animalId + " not found.", HttpStatus.NOT_FOUND);
        }
        List<AppointmentResponseDTO> appointmentResponseDTOList =
                appointmentService.getAllAppointmentByPatient(patient.get());
        if (appointmentResponseDTOList.isEmpty()) {
            log.error("Any appointment for patient with id={} not found.", animalId);
            return new ResponseEntity<>("Any appointment for patient with id " + animalId + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        log.info("Return appointment list for patient with id={}", animalId);
        return new ResponseEntity<>(appointmentResponseDTOList, HttpStatus.OK);
    }

    @PatchMapping(path = "{appointmentId}")
    public ResponseEntity<?> completeAppointmentAfterFinished(@PathVariable("appointmentId") Long appointmentId,
                                                              @RequestBody AppointmentInfoRequestDTO appointmentInfoRequestDTO) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment.isEmpty()) {
            log.error("Appointment with id={} not found.", appointmentId);
            return new ResponseEntity<>("Appointment with id " + appointmentId + " not found.", HttpStatus.NOT_FOUND);
        }
        AppointmentResponseDTO appointmentResponseDTO =
                appointmentService.addInfoAfterAppointment(appointment.get(), appointmentInfoRequestDTO);
        log.info("Add description={} and appointmentStatus={} to appointment with id={}.",
                appointmentResponseDTO.getAppointmentDescription(),
                appointmentResponseDTO.getAppointmentStatus(),
                appointmentResponseDTO.getAppointmentId());
        return new ResponseEntity<>(appointmentResponseDTO, HttpStatus.OK);
    }
}

