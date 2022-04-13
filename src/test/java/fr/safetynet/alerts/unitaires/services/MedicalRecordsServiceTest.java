package fr.safetynet.alerts.unitaires.services;

import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import fr.safetynet.alerts.service.FireStationsService;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
import fr.safetynet.alerts.tools.JsonTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class MedicalRecordsServiceTest {
    MedicalRecordsRepo medicalRecordsRepo = MedicalRecordsRepo.getInstance();
    @Autowired
    private MedicalRecordsService medicalRecordsService;

    @MockBean
    JsonTools jsonTools;

    @BeforeEach
    void prepareTest() {
        jsonTools = new JsonTools(new FireStationsService(), new MedicalRecordsService(), new PersonsService());
        jsonTools.parseFireStations();
        jsonTools.parseMedicalRecords();
        jsonTools.parsePerson();
    }

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
    public void addMedicalRecordAlreadyExistTest() throws ParseException {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Already");
        input.setLastName("Exist");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        JSONObject result = medicalRecordsService.addMedicalRecord(input);
        assertTrue(!result.isEmpty());
        assertEquals(result.get("firstName"), "Already");
        assertEquals(result.get("allergies").toString(), "[]");
        assertThrows(AlreadyExistException.class, () -> medicalRecordsService.addMedicalRecord(input));
    }

    @Test
    public void alreadyExistWithFirstNameTest()   {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("John");
        input.setLastName("Unknown");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertFalse(medicalRecordsService.alreadyExist(input));
    }

    @Test
    public void alreadyExistWithLastNameTest()   {
        MedicalRecord input = new MedicalRecord();
        input.setFirstName("Unknown");
        input.setLastName("Boyd");
        input.setBirthdate("01/12/2001");
        input.setMedications(new ArrayList());
        input.setAllergies(new ArrayList());
        assertFalse(medicalRecordsService.alreadyExist(input));
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

    @Test
    public void getMedicalRecordByNameEmpty() {
        MedicalRecord result = medicalRecordsRepo.getMedicalRecordByNameAndFirstName("Unknown", "Unknown");
        assertEquals(result.toString(), new MedicalRecord().toString());
        assertTrue(result.getFirstName() == null);
    }
}


