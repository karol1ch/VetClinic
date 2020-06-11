package com.chomoncik.clinic.repository;

import com.chomoncik.clinic.model.Animal;
import com.chomoncik.clinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentByAppointmentDate(LocalDate date);
    List<Appointment> findAppointmentByPatient(Animal animal);
}
