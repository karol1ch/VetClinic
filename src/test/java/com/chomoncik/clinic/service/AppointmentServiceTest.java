package com.chomoncik.clinic.service;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import com.chomoncik.clinic.model.AppointmentStatus;
import com.chomoncik.clinic.model.AppointmentTime;
import com.chomoncik.clinic.model.DTO.AppointmentRequestDTO;
import com.chomoncik.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private static LocalDateTime APPOINTMENT_DATETIME_BEFORE_TODAY = LocalDateTime.parse("2020-05-19T10:00:00");
    private static LocalDateTime APPOINTMENT_DATETIME_AFTER_TODAY = LocalDateTime.parse("2020-05-21T10:00:00");
    private static LocalDateTime TODAY_DATETIME = LocalDateTime.parse("2020-05-20T10:00:00");
    private final String APPOINTMENT_DESCRIPTION = "This is description of appointment.";
    private static final String ANIMAL_NAME = "tofik";
    private static final String SPECIES = "dog";
    private static final int BIRTH_YEAR = 2010;
    private final Animal ANIMAL_ON_APPOINTMENT = Animal.builder()
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
            .build();

    private static final AppointmentRequestDTO APPOINTMENT_REQUEST_DTO = AppointmentRequestDTO.builder()
            .appointmentDateTime(APPOINTMENT_DATETIME_AFTER_TODAY)
            .appointmentTime(AppointmentTime.FIFTEEN)
            .patientId(1L)
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
    void shouldAddAppointment(){
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
    void shouldReturnFalseWhenDateIsNotAfterToday() {
        //GIVEN
        given(clock.instant()).willReturn(FIXED_CLOCK.instant());
        given(clock.getZone()).willReturn(FIXED_CLOCK.getZone());

        //WHEN
        Boolean isFalse = appointmentService.checkIfDateIsAtLeastTomorrow(APPOINTMENT_DATETIME_BEFORE_TODAY);

        //THEN
        assertThat(isFalse).isEqualTo(false);
    }
}