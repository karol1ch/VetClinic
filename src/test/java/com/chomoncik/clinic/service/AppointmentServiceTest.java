package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.AppointmentStatus;
import com.chomoncik.clinic.model.AppointmentTime;
import com.chomoncik.clinic.model.dto.AppointmentInfoRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentRequestDTO;
import com.chomoncik.clinic.model.dto.AppointmentResponseDTO;
import com.chomoncik.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

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

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldAddAppointment() {
        //GIVEN
        given(appointmentRepository.save(any(Appointment.class))).willReturn(ARRANGED_APPOINTMENT);

        //WHEN
        Appointment appointment = appointmentService
                .addAppointment(APPOINTMENT_REQUEST_DTO, ANIMAL_ON_APPOINTMENT);

        //THEN
        assertThat(appointment).isEqualTo(ARRANGED_APPOINTMENT);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldGetNullWhenAppointmentNotExist() {
        //GIVEN
        given(appointmentRepository.findById(APPOINTMENT_ID + 1)).willReturn(null);

        //WHEN
        Optional<Appointment> appointment = appointmentService
                .getAppointmentById(APPOINTMENT_ID + 1);

        //THEN
        assertThat(appointment).isNull();
    }

    @Test
    void shouldGetAppointmentById() {
        //GIVEN
        given(appointmentRepository.findById(APPOINTMENT_ID)).willReturn(Optional.ofNullable(ARRANGED_APPOINTMENT));

        //WHEN
        Appointment appointment = appointmentService
                .getAppointmentById(APPOINTMENT_ID).get();

        //THEN
        assertThat(appointment).isEqualTo(ARRANGED_APPOINTMENT);
    }

    @Test
    void shouldGetAppointmentResponseDTOById() {
        //GIVEN
        given(appointmentRepository.findById(APPOINTMENT_ID))
                .willReturn(Optional.ofNullable(ARRANGED_APPOINTMENT));

        //WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentService
                .getAppointmentResponseDTOById(APPOINTMENT_ID).get();

        //THEN
        assertThat(appointmentResponseDTO).isEqualTo(ARRANGED_APPOINTMENT_RESPONSE_DTO);
    }

    @Test
    void shouldGetAllAppointmentsWhenExists() {
        //GIVEN
        given(appointmentRepository.findAll()).willReturn(List.of(ARRANGED_APPOINTMENT));

        //WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOList = appointmentService.getAllAppointments();

        //THEN
        appointmentResponseDTOList.forEach(x -> assertThat(x).isEqualTo(ARRANGED_APPOINTMENT_RESPONSE_DTO));
        assertThat(appointmentResponseDTOList.size()).isEqualTo(1);
    }

    @Test
    void shouldGetEmptyListWhenAppointmentsNotExists() {
        //GIVEN
        given(appointmentRepository.findAll()).willReturn(Collections.emptyList());

        //WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOList = appointmentService.getAllAppointments();

        //THEN
        assertThat(appointmentResponseDTOList).isEmpty();
        assertThat(appointmentResponseDTOList.size()).isEqualTo(0);
    }

    @Test
    void shouldGetAllAppointmentByDate() {
        //GIVEN
        given(appointmentRepository.findAppointmentByAppointmentDate(APPOINTMENT_DATE_AFTER_TODAY))
                .willReturn(List.of(ARRANGED_APPOINTMENT, SECOND_ARRANGED_APPOINTMENT));

        //WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOList = appointmentService.getAllAppointmentsByDate(APPOINTMENT_DATE_AFTER_TODAY);

        //THEN
        assertThat(appointmentResponseDTOList.get(0)).isEqualTo(ARRANGED_APPOINTMENT_RESPONSE_DTO);
        assertThat(appointmentResponseDTOList.get(1)).isEqualTo(SECOND_ARRANGED_APPOINTMENT_RESPONSE_DTO);
        assertThat(appointmentResponseDTOList.size()).isEqualTo(2);
    }

    @Test
    void shouldGetEmptyListWhenAppointmentsInThisDateNotExists() {
        //GIVEN
        given(appointmentRepository.findAppointmentByAppointmentDate(APPOINTMENT_DATE_AFTER_TODAY))
                .willReturn(Collections.emptyList());

        //WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOList = appointmentService.getAllAppointmentsByDate(APPOINTMENT_DATE_AFTER_TODAY);

        //THEN
        assertThat(appointmentResponseDTOList).isEmpty();
        assertThat(appointmentResponseDTOList.size()).isEqualTo(0);
    }

    @Test
    void shouldGetEmptyListWhenAppointmentsForPatientNotExists() {
        //GIVEN
        given(appointmentRepository.findAppointmentByPatient(ANIMAL_ON_APPOINTMENT))
                .willReturn(Collections.emptyList());

        //WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOList =
                appointmentService.getAllAppointmentByPatient(ANIMAL_ON_APPOINTMENT);

        //THEN
        assertThat(appointmentResponseDTOList).isEmpty();
        assertThat(appointmentResponseDTOList.size()).isEqualTo(0);
    }

    @Test
    void shouldGetAllAppointmentByPatient() {
        //GIVEN
        given(appointmentRepository.findAppointmentByPatient(ANIMAL_ON_APPOINTMENT))
                .willReturn(List.of(ARRANGED_APPOINTMENT, SECOND_ARRANGED_APPOINTMENT));

        //WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOList =
                appointmentService.getAllAppointmentByPatient(ANIMAL_ON_APPOINTMENT);

        //THEN
        assertThat(appointmentResponseDTOList.get(0)).isEqualTo(ARRANGED_APPOINTMENT_RESPONSE_DTO);
        assertThat(appointmentResponseDTOList.get(1)).isEqualTo(SECOND_ARRANGED_APPOINTMENT_RESPONSE_DTO);
        assertThat(appointmentResponseDTOList.size()).isEqualTo(2);
    }

    @Test
    void shouldAddInfoToAppointmentWhenNoOneCome() {
        //GIVEN
        given(appointmentRepository.save(any(Appointment.class))).willReturn(APPOINTMENT_NOBODY_COME);

        //WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentService
                .addInfoAfterAppointment(ARRANGED_APPOINTMENT, APPOINTMENT_INFO_NOBODY_COME_REQUEST_DTO);

        //THEN
        assertThat(appointmentResponseDTO).isEqualTo(APPOINTMENT_NOBODY_COME_RESPONSE_DTO);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldAddInfoToAppointmentWhenCompletedWithoutDescription() {
        //GIVEN
        given(appointmentRepository.save(any(Appointment.class))).willReturn(APPOINTMENT_COMPLETED_WITHOUT_DESC);

        //WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentService
                .addInfoAfterAppointment(ARRANGED_APPOINTMENT, APPOINTMENT_INFO_COMPLETED_WITHOUT_DESC_REQUEST_DTO);

        //THEN
        assertThat(appointmentResponseDTO).isEqualTo(APPOINTMENT_COMPLETED_WITHOUT_DESC_RESPONSE_DTO);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldCreateLocalDateFromValuesWhenDateIsCorrect() {
        //GIVEN
        int year = 2020;
        int month = 1;
        int day = 1;

        //WHEN
        Optional<LocalDate> localDate = appointmentService.createLocalDateFromValues(year, month, day);

        //THEN
        assertThat(localDate.get()).isEqualTo(LocalDate.of(year, month, day));
    }

    @Test
    void shouldReturnEmptyOptionalWhenDateIsCorrect() {
        //GIVEN
        int year = 2020;
        int month = 14;
        int day = 1;

        //WHEN
        Optional<LocalDate> localDate = appointmentService.createLocalDateFromValues(year, month, day);

        //THEN
        assertThat(localDate).isEmpty();
    }

    @Test
    void shouldReturnFalseWhenDateIsNotAfterToday() {
        //GIVEN
        given(clock.instant()).willReturn(FIXED_CLOCK.instant());
        given(clock.getZone()).willReturn(FIXED_CLOCK.getZone());

        //WHEN
        Boolean isFalse = appointmentService.checkIfDateIsAtLeastTomorrow(
                LocalDateTime.of(APPOINTMENT_DATE_BEFORE_TODAY, APPOINTMENT_HOUR));

        //THEN
        assertThat(isFalse).isEqualTo(false);
    }

    @Test
    void shouldReturnTrueWhenDateIsAfterToday() {
        //GIVEN
        given(clock.instant()).willReturn(FIXED_CLOCK.instant());
        given(clock.getZone()).willReturn(FIXED_CLOCK.getZone());

        //WHEN
        Boolean isTrue = appointmentService.checkIfDateIsAtLeastTomorrow(
                LocalDateTime.of(APPOINTMENT_DATE_AFTER_TODAY, APPOINTMENT_HOUR));

        //THEN
        assertThat(isTrue).isEqualTo(true);
    }

    @Test
    void shouldReturnTrueWhenTimeIsInWorkHours() {
        //WHEN
        Boolean isTrue = appointmentService
                .checkIfAppointmentIsInWorkHours(APPOINTMENT_HOUR, AppointmentTime.FIFTEEN.getValue());

        //THEN
        assertThat(isTrue).isEqualTo(true);
    }

    @Test
    void shouldReturnFalseWhenEndTimeIsNotInWorkHours() {
        //WHEN
        Boolean isFalse = appointmentService
                .checkIfAppointmentIsInWorkHours(APPOINTMENT_HOUR.plusHours(1), AppointmentTime.SIXTY.getValue());

        //THEN
        assertThat(isFalse).isEqualTo(false);
    }

    @Test
    void shouldReturnFalseWhenAllAppointmentIsNotInWorkHours() {
        //WHEN
        Boolean isFalse = appointmentService
                .checkIfAppointmentIsInWorkHours(APPOINTMENT_HOUR_AFTER_WORK_TIME, AppointmentTime.SIXTY.getValue());

        //THEN
        assertThat(isFalse).isEqualTo(false);
    }

    @Test
    void shouldReturnTrueWhenAppointmentHourIsFree() {
        //GIVEN
        given(appointmentRepository.findAppointmentByAppointmentDate(APPOINTMENT_DATE_AFTER_TODAY))
                .willReturn(List.of(ARRANGED_APPOINTMENT, SECOND_ARRANGED_APPOINTMENT));

        //WHEN
        Boolean isTrue = appointmentService
                .checkIfAppointmentTimeIsFree(LocalDateTime.of(APPOINTMENT_DATE_AFTER_TODAY, APPOINTMENT_HOUR_FREE), AppointmentTime.FIFTEEN.getValue());

        //THEN
        assertThat(isTrue).isEqualTo(true);
    }

    @Test
    void shouldReturnFalseWhenAppointmentHourIsNotFree() {
        //GIVEN
        given(appointmentRepository.findAppointmentByAppointmentDate(APPOINTMENT_DATE_AFTER_TODAY))
                .willReturn(List.of(ARRANGED_APPOINTMENT, SECOND_ARRANGED_APPOINTMENT));

        //WHEN
        Boolean isFalse = appointmentService
                .checkIfAppointmentTimeIsFree(LocalDateTime.of(APPOINTMENT_DATE_AFTER_TODAY, APPOINTMENT_HOUR_NOT_FREE), AppointmentTime.FIFTEEN.getValue());

        //THEN
        assertThat(isFalse).isEqualTo(false);
    }

}