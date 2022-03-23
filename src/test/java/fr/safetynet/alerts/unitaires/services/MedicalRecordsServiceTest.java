package fr.safetynet.alerts.unitaires.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.service.MedicalRecordsService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MedicalRecordsServiceTest {
    @Autowired
    private MedicalRecordsService medicalRecordsService;

    @Test
    public void addMedicalRecordTest() throws ParseException {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Firstname");
        input.setLastName("Lastname");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        JSONObject result = medicalRecordsService.addMedicalRecord(input);
        assertTrue(!result.isEmpty());
        assertEquals(result.get("firstName"), "Firstname");
        assertEquals(result.get("allergies").toString(), "[]");
    }

    @Test
    public void addMedicalRecordWithoutFirstNameTest() {
        MedicalRecord input = new MedicalRecord();
        input.setLastName("Lastname");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.addMedicalRecord(input));
    }

    @Test
    public void addMedicalRecordWithoutLastNameTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Firstname");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.addMedicalRecord(input));
    }

    @Test
    public void addMedicalRecordWithoutBirthdateTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Firstname");
        input.setLastName("Lastname");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.addMedicalRecord(input));
    }

    @Test
    public void addMedicalRecordWithoutMedicationTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Firstname");
        input.setLastName("Lastname");
        input.setBirthdate("01/12/2001");
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.addMedicalRecord(input));
    }

    @Test
    public void addMedicalRecordWithoutAllergiesTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Firstname");
        input.setLastName("Lastname");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.addMedicalRecord(input));
    }

    @Test
    public void modifyMedicalRecordTest() throws ParseException {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setLastName("Boyd");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        JSONObject result = medicalRecordsService.modifyMedicalRecord(input);
        assertFalse(result.isEmpty());
        assertEquals(result.get("firstName"), "John");
        assertEquals(result.get("birthdate"), "01/12/2001");
    }

    @Test
    public void modifyMedicalRecordInvalidInputWithoutFirstNameTest() {
        MedicalRecord input = new MedicalRecord();
        input.setLastName("Boyd");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.modifyMedicalRecord(input));
    }

    @Test
    public void modifyMedicalRecordInvalidInputWithoutLastNameTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.modifyMedicalRecord(input));
    }

    @Test
    public void modifyMedicalRecordInvalidInputWithoutBirthdateTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setLastName("Boyd");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.modifyMedicalRecord(input));
    }

    @Test
    public void modifyMedicalRecordInvalidInputWithoutMedicationsTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setLastName("Boyd");
        input.setBirthdate("01/12/2001");
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.modifyMedicalRecord(input));
    }

    @Test
    public void modifyMedicalRecordInvalidInputWithoutAllergiesTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setLastName("Boyd");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.modifyMedicalRecord(input));
    }

    @Test
    public void modifyMedicalRecordNotFoundTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Unknown");
        input.setLastName("Unknown");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(NotFoundException.class, () -> medicalRecordsService.modifyMedicalRecord(input));
    }

    @Test
    public void deleteMedicalRecordTest() throws ParseException {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setLastName("Boyd");
        input.setBirthdate("");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        JSONObject result = medicalRecordsService.deleteMedicalRecord(input);
        assertFalse(result.isEmpty());
        assertEquals(result.get("firstName"), "John");
        assertEquals(result.get("lastName"), "Boyd");
    }

    @Test
    public void deleteMedicalRecordInvalidInputWithoutFirstNameTest() throws ParseException {
        MedicalRecord input = new MedicalRecord();
        input.setLastName("Boyd");
        input.setBirthdate("");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.deleteMedicalRecord(input));
    }

    @Test
    public void deleteMedicalRecordInvalidInputWithoutLastNameTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setBirthdate("");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(InvalidInputException.class, () -> medicalRecordsService.deleteMedicalRecord(input));
    }

    @Test
    public void deleteMedicalRecordNotFoundTest() {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Unknown");
        input.setLastName("Unknown");
        input.setBirthdate("");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertThrows(NotFoundException.class, () -> medicalRecordsService.deleteMedicalRecord(input));
    }
}
