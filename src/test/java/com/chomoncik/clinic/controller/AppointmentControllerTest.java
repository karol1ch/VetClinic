package com.chomoncik.clinic.controller;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.AppointmentStatus;
import com.chomoncik.clinic.model.AppointmentTime;
import com.chomoncik.clinic.model.dto.AppointmentInfoRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentResponseDTO;
import com.chomoncik.clinic.service.AnimalService;
import com.chomoncik.clinic.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.util.Lists;
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


import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    private static long APPOINTMENT_ID = 1L;
    private static long ANIMAL_ID = 1L;
    private static LocalDate APPOINTMENT_DATE_BEFORE_TODAY = LocalDate.of(2020, 9, 1);
    private static LocalDate APPOINTMENT_DATE_AFTER_TODAY = LocalDate.of(2020, 12, 1);
    private static LocalDate TODAY_DATE = LocalDate.of(2020, 10, 30);
    private static LocalTime NOW_TIME = LocalTime.of(8, 0, 0);
    private static LocalTime APPOINTMENT_HOUR = LocalTime.of(18, 30, 0);
    private static LocalTime APPOINTMENT_HOUR_AFTER_WORK_TIME = LocalTime.of(21, 0, 0);
    private static LocalTime APPOINTMENT_HOUR_BEFORE_WORK_TIME = LocalTime.of(5, 0, 0);
    private static LocalTime APPOINTMENT_HOUR_FREE = LocalTime.of(11, 0, 0);
    private static LocalTime APPOINTMENT_HOUR_NOT_FREE = LocalTime.of(18, 40, 0);
    private static LocalDateTime TODAY_DATETIME = LocalDateTime.of(TODAY_DATE, NOW_TIME);
    private static final String APPOINTMENT_DESCRIPTION = "description";
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
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentHour(APPOINTMENT_HOUR)
            .appointmentStatus(AppointmentStatus.ARRANGED)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patient(ANIMAL_ON_APPOINTMENT)
            .build();
    private static final Appointment SECOND_ARRANGED_APPOINTMENT = Appointment.builder()
            .appointmentId(APPOINTMENT_ID + 1)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentHour(APPOINTMENT_HOUR.plusHours(1))
            .appointmentStatus(AppointmentStatus.ARRANGED)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patient(ANIMAL_ON_APPOINTMENT)
            .build();
    private static final Appointment APPOINTMENT_NOBODY_COME = Appointment.builder()
            .appointmentId(APPOINTMENT_ID)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentHour(APPOINTMENT_HOUR)
            .appointmentStatus(AppointmentStatus.NOBODY_CAME)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patient(ANIMAL_ON_APPOINTMENT)
            .description(APPOINTMENT_DESCRIPTION)
            .build();
    private static final Appointment APPOINTMENT_COMPLETED_WITHOUT_DESC = Appointment.builder()
            .appointmentId(APPOINTMENT_ID)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentHour(APPOINTMENT_HOUR)
            .appointmentStatus(AppointmentStatus.COMPLETED)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patient(ANIMAL_ON_APPOINTMENT)
            .build();
    private static final AppointmentRequestDTO APPOINTMENT_REQUEST_DTO = AppointmentRequestDTO.builder()
            .appointmentDateTime(LocalDateTime.of(APPOINTMENT_DATE_AFTER_TODAY, APPOINTMENT_HOUR))
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patientId(1L)
            .build();
    private static final AppointmentResponseDTO ARRANGED_APPOINTMENT_RESPONSE_DTO = AppointmentResponseDTO.builder()
            .appointmentId(APPOINTMENT_ID)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentStartTime(APPOINTMENT_HOUR)
            .appointmentEndTime(APPOINTMENT_HOUR.plusMinutes(AppointmentTime.FIFTEEN.getValue()))
            .appointmentStatus(AppointmentStatus.ARRANGED.name())
            .patientId(ANIMAL_ID)
            .build();
    private static final AppointmentResponseDTO SECOND_ARRANGED_APPOINTMENT_RESPONSE_DTO = AppointmentResponseDTO.builder()
            .appointmentId(APPOINTMENT_ID + 1)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentStartTime(APPOINTMENT_HOUR.plusHours(1))
            .appointmentEndTime(APPOINTMENT_HOUR.plusHours(1).plusMinutes(AppointmentTime.FIFTEEN.getValue()))
            .appointmentStatus(AppointmentStatus.ARRANGED.name())
            .patientId(ANIMAL_ID)
            .build();
    private static final AppointmentResponseDTO APPOINTMENT_NOBODY_COME_RESPONSE_DTO = AppointmentResponseDTO.builder()
            .appointmentId(APPOINTMENT_ID)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentStartTime(APPOINTMENT_HOUR)
            .appointmentEndTime(APPOINTMENT_HOUR.plusMinutes(AppointmentTime.FIFTEEN.getValue()))
            .appointmentStatus(AppointmentStatus.NOBODY_CAME.name())
            .appointmentDescription(APPOINTMENT_DESCRIPTION)
            .patientId(ANIMAL_ID)
            .build();
    private static final AppointmentResponseDTO APPOINTMENT_COMPLETED_WITHOUT_DESC_RESPONSE_DTO = AppointmentResponseDTO.builder()
            .appointmentId(APPOINTMENT_ID)
            .appointmentDate(APPOINTMENT_DATE_AFTER_TODAY)
            .appointmentStartTime(APPOINTMENT_HOUR)
            .appointmentEndTime(APPOINTMENT_HOUR.plusMinutes(AppointmentTime.FIFTEEN.getValue()))
            .appointmentStatus(AppointmentStatus.COMPLETED.name())
            .patientId(ANIMAL_ID)
            .build();
    private static final AppointmentInfoRequestDTO APPOINTMENT_INFO_NOBODY_COME_REQUEST_DTO =
            AppointmentInfoRequestDTO.builder()
                    .isCompleted(false)
                    .description(APPOINTMENT_DESCRIPTION)
                    .build();
    private static final AppointmentInfoRequestDTO APPOINTMENT_INFO_COMPLETED_WITHOUT_DESC_REQUEST_DTO =
            AppointmentInfoRequestDTO.builder()
                    .isCompleted(true)
                    .build();
    private static final Clock FIXED_CLOCK =
            Clock.fixed(TODAY_DATETIME.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

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
        given(appointmentService.checkIfAppointmentIsInWorkHours(any(LocalTime.class), anyInt())).willReturn(true);
        given(appointmentService.checkIfAppointmentTimeIsFree(any(LocalDateTime.class), anyInt())).willReturn(true);
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
    void shouldReturnBadRequestWhenDateIsNotAtLeastTomorrow() throws Exception {
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
        assertThat(response.getContentAsString()).contains("Given appointment date " +
                LocalDateTime.of(APPOINTMENT_DATE_AFTER_TODAY, APPOINTMENT_HOUR) +
                " is not after today.");
    }

    @Test
    void shouldReturnBadRequestWhenTimeIsNotInWorkHours() throws Exception {
        //GIVEN
        given(appointmentService.checkIfDateIsAtLeastTomorrow(any())).willReturn(true);
        given(appointmentService.checkIfAppointmentIsInWorkHours(any(LocalTime.class), anyInt())).willReturn(false);

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
        assertThat(response.getContentAsString()).contains(
                "Given appointment time: " + APPOINTMENT_HOUR + " - " +
                        APPOINTMENT_HOUR.plusMinutes(AppointmentTime.FIFTEEN.getValue()) + " is not between 8AM and 8PM."
        );
    }

    @Test
    void shouldReturnConflictWhenTimeIsNotFree() throws Exception {
        //GIVEN
        given(appointmentService.checkIfDateIsAtLeastTomorrow(any())).willReturn(true);
        given(appointmentService.checkIfAppointmentIsInWorkHours(any(LocalTime.class), anyInt())).willReturn(true);
        given(appointmentService.checkIfAppointmentTimeIsFree(any(LocalDateTime.class), anyInt())).willReturn(false);
        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(APPOINTMENT_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getContentAsString()).contains(
                "Given appointment datetime " + LocalDateTime.of(APPOINTMENT_DATE_AFTER_TODAY, APPOINTMENT_HOUR) +
                        " is not available."
        );
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAnimalNotExist() throws Exception {
        //GIVEN
        given(appointmentService.checkIfDateIsAtLeastTomorrow(any())).willReturn(true);
        given(appointmentService.checkIfAppointmentIsInWorkHours(any(LocalTime.class), anyInt())).willReturn(true);
        given(appointmentService.checkIfAppointmentTimeIsFree(any(LocalDateTime.class), anyInt())).willReturn(true);
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

    @Test
    void shouldGetAppointmentResponseDTOById() throws Exception {
        //GIVEN
        given(appointmentService.getAppointmentResponseDTOById(1L)).willReturn(Optional.of(ARRANGED_APPOINTMENT_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/{appointmentId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(AppointmentStatus.ARRANGED.name());
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAppointmentNotExist() throws Exception {
        //GIVEN
        given(appointmentService.getAppointmentResponseDTOById(1L)).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/{appointmentId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Appointment with id 1 not found.");
    }

    @Test
    void shouldGetAllAppointments() throws Exception {
        //GIVEN
        given(appointmentService.getAllAppointments()).willReturn(List.of(
                ARRANGED_APPOINTMENT_RESPONSE_DTO, SECOND_ARRANGED_APPOINTMENT_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"appointmentId\":1");
        assertThat(response.getContentAsString()).contains("\"appointmentId\":2");
    }

    @Test
    void shouldReturnNotFoundExceptionWhenAppointmentsListIsEmpty() throws Exception {
        //GIVEN
        given(appointmentService.getAllAppointments()).willReturn(Lists.emptyList());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Appointment list is empty.");
    }

    @Test
    void shouldReturnBadRequestWhenAppointmentsDateIsNotCorrect() throws Exception {
        //GIVEN
        int year = 2020;
        int month = 13;
        int day = 1;
        given(appointmentService.createLocalDateFromValues(year, month, day)).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/at_date")
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .param("day", String.valueOf(day)))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Date not exist.");
    }

    @Test
    void shouldReturnNotFoundWhenAppointmentsInDateNotExists() throws Exception {
        //GIVEN
        int year = 2020;
        int month = 1;
        int day = 1;
        given(appointmentService.createLocalDateFromValues(year, month, day))
                .willReturn(Optional.of(LocalDate.of(year, month, day)));
        given(appointmentService.getAllAppointmentsByDate(any(LocalDate.class)))
                .willReturn(Lists.emptyList());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/at_date")
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .param("day", String.valueOf(day)))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString())
                .contains("Any appointment with date " + LocalDate.of(year, month, day) + " not found.");
    }

    @Test
    void shouldReturnAppointmentsInDate() throws Exception {
        //GIVEN
        int year = 2020;
        int month = 12;
        int day = 1;
        given(appointmentService.createLocalDateFromValues(year, month, day))
                .willReturn(Optional.of(LocalDate.of(year, month, day)));
        given(appointmentService.getAllAppointmentsByDate(any(LocalDate.class)))
                .willReturn(List.of(ARRANGED_APPOINTMENT_RESPONSE_DTO, SECOND_ARRANGED_APPOINTMENT_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/at_date")
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .param("day", String.valueOf(day)))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"appointmentId\":1");
        assertThat(response.getContentAsString()).contains("\"appointmentId\":2");
    }

    @Test
    void shouldReturnNotFoundWhenPatientNotExists() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.empty());

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/patient/{animalId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Animal with id " + ANIMAL_ID + " not found.");
    }

    @Test
    void shouldGetAllAppointmentsForPatient() throws Exception {
        //GIVEN
        given(animalService.getAnimalById(anyLong())).willReturn(Optional.ofNullable(ANIMAL_ON_APPOINTMENT));
        given(appointmentService.getAllAppointmentByPatient(any(Animal.class))).willReturn(List.of(
                ARRANGED_APPOINTMENT_RESPONSE_DTO, SECOND_ARRANGED_APPOINTMENT_RESPONSE_DTO));

        //WHEN
        MockHttpServletResponse response = mockMvc.perform(
                get("/appointment/patient/{animalId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //THEN
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"appointmentId\":1");
        assertThat(response.getContentAsString()).contains("\"appointmentId\":2");
    }
}