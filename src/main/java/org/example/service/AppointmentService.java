package org.example.service;

import org.example.model.Appointment;
import org.example.repository.AppointmentRepository;

import java.util.List;
import java.util.Map;

public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    // Dependency Injection සඳහා (Tests වලදී Mock එක ලබා දෙන්න භාවිතා කරයි)
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Default Constructor (Servlet එකේදී සැබෑ Repository එක සාදාගන්න භාවිතා කරයි)
    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
    }

    public boolean addAppointment(Appointment appointment) {
        // Business Validation: Patient ID සහ Dentist ID නිවැරදි (ධන) අගයන් විය යුතුයි
        if (appointment.getPatientId() <= 0 || appointment.getDentistId() <= 0) {
            return false;
        }

        return appointmentRepository.addAppointment(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.getAllAppointments();
    }

    public boolean deleteAppointment(int id) {
        return appointmentRepository.deleteAppointment(id);
    }

    public Map<String, Object> getAppointmentWithPatientDetails(int appointmentId) {
        return appointmentRepository.getAppointmentWithPatientDetails(appointmentId);
    }
}