package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.AppointmentStatus;
import com.chomoncik.clinic.model.AppointmentTime;
import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.service.AnimalService;
import com.chomoncik.clinic.service.AppointmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    private static long APPOINTMENT_ID = 1L;
    private static long ANIMAL_ID = 1L;
    private static LocalDateTime APPOINTMENT_DATETIME_BEFORE_TODAY = LocalDateTime.parse("2020-05-19T10:00:00");
    private static LocalDateTime APPOINTMENT_DATETIME_AFTER_TODAY = LocalDateTime.parse("2020-05-21T10:00:00");
    private static LocalDateTime TODAY_DATETIME = LocalDateTime.parse("2020-05-20T10:00:00");
    private final String APPOINTMENT_DESCRIPTION = "This is description of appointment.";
    private static final String ANIMAL_NAME = "tofik";
    private static final String SPECIES = "dog";
    private static final int BIRTH_YEAR = 2010;
    private static final Animal ANIMAL_ON_APPOINTMENT = Animal.builder()
            .animalId(ANIMAL_ID)
            .animalName(ANIMAL_NAME)
            .species(SPECIES)
            .birthYear(BIRTH_YEAR)
            .build();
    private static final Appointment ARRANGED_APPOINTMENT = Appointment.builder()
            .appointmentId(APPOINTMENT_ID)
            .appointmentDateTime(APPOINTMENT_DATETIME_AFTER_TODAY)
            .appointmentStatus(AppointmentStatus.ARRANGED)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patient(ANIMAL_ON_APPOINTMENT)
            .build();

    private static final AppointmentRequestDTO APPOINTMENT_REQUEST_DTO = AppointmentRequestDTO.builder()
            .appointmentDateTime(APPOINTMENT_DATETIME_AFTER_TODAY)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patientId(1L)
            .build();

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    private void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController)
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void shouldAddAppointment() throws Exception {
        //GIVEN
        given(appointmentService.checkIfDateIsAtLeastTomorrow(any())).willReturn(true);
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.of(ANIMAL_ON_APPOINTMENT));
        given(appointmentService.addAppointment(any(), any())).willReturn(ARRANGED_APPOINTMENT);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(APPOINTMENT_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains(AppointmentStatus.ARRANGED.toString());
    }

    @Test
    void shouldReturnBadRequestWhenDateIsNotValid() throws Exception {
        //GIVEN
        given(appointmentService.checkIfDateIsAtLeastTomorrow(any())).willReturn(false);

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                post("/appointment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(APPOINTMENT_REQUEST_DTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAnimalNotExist() throws Exception {
        //GIVEN
        given(appointmentService.checkIfDateIsAtLeastTomorrow(any())).willReturn(true);
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(APPOINTMENT_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id " + ANIMAL_ID + " not found.");
    }

}