package project1.project1.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import project1.project1.DAO.PatientRepository;
import project1.project1.control.PatientController;
import project1.project1.model.Appointment;
import project1.project1.model.Patient;
import project1.project1.service.AppointmentService;
import project1.project1.service.PatientService;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PatientController patientController;

    @Test
    void dashboard_onlyCancelledAppointmentsInCancelledList() {
        // Arrange
        Patient patient = new Patient();
        patient.setPatientId(1L);
        patient.setEmail("test@example.com");

        Appointment cancelled = new Appointment();
        cancelled.setStatus(Appointment.Status.CANCELLED);

        when(authentication.getName()).thenReturn("test@example.com");
        when(patientRepository.findByEmail("test@example.com"))
                .thenReturn(List.of(patient));
        when(appointmentService.viewCancelledAppointmentsByPatient(1L))
                .thenReturn(List.of(cancelled));
        when(appointmentService.viewActiveAppointmentsByPatient(1L))
                .thenReturn(List.of());

        Model model = new ExtendedModelMap();

        // Act
        patientController.dashboard(model, "", authentication);

        // Assert
        List<Appointment> cancelledAppointments =
                (List<Appointment>) model.getAttribute("cancelledAppointments");
        assertEquals(1, cancelledAppointments.size());
        assertEquals(Appointment.Status.CANCELLED, cancelledAppointments.get(0).getStatus());
    }
}
