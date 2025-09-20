package project1.project1.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import project1.project1.DAO.PatientRepository;
import project1.project1.model.Patient;
import project1.project1.service.PatientService;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientService patientService;

    /**
     * TC02 - Register patient with duplicate email
     */
    @Test
    void registerPatient_duplicateEmail_throwsException() {
        Patient existing = new Patient();
        existing.setEmail("test@example.com");

        // Return a list with one patient (matches repository signature)
        when(patientRepository.findByEmail("test@example.com"))
                .thenReturn(List.of(existing));

        Patient newPatient = new Patient();
        newPatient.setEmail("test@example.com");

        assertThrows(RuntimeException.class, () -> patientService.registerPatient(newPatient));
        verify(patientRepository, never()).save(any());
    }

    /**
     * TC03 - Login with correct email & password (BCrypt)
     */
    @Test
    void loginPatient_correctCredentials_returnsPatient() {
        String rawPassword = "Password123";
        String encodedPassword = "$2a$10$abcdef..."; // dummy hash

        Patient patient = new Patient();
        patient.setEmail("test@example.com");
        patient.setPassword(encodedPassword);

        when(patientRepository.findByEmail("test@example.com"))
                .thenReturn(List.of(patient));
        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);

        Patient result = patientService.loginPatient("test@example.com", rawPassword);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    /**
     * TC04 - Login with wrong password (BCrypt)
     */
    @Test
    void loginPatient_wrongPassword_throwsException() {
        String rawPassword = "WrongPass";
        String encodedPassword = "$2a$10$abcdef...";

        Patient patient = new Patient();
        patient.setEmail("test@example.com");
        patient.setPassword(encodedPassword);

        when(patientRepository.findByEmail("test@example.com"))
                .thenReturn(List.of(patient));
        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> patientService.loginPatient("test@example.com", rawPassword));
    }
}
