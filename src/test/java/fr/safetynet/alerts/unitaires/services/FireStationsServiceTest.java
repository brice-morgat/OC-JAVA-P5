package fr.safetynet.alerts.unitaires.services;


import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.service.FireStationsService;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
import fr.safetynet.alerts.tools.JsonTools;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FireStationsServiceTest {

    @Autowired
    private FireStationsService fireStationsService;

    @MockBean
    JsonTools jsonTools;

    @BeforeEach
    void prepareTest() {
        jsonTools = new JsonTools(new FireStationsService(), new MedicalRecordsService(), new PersonsService());
        jsonTools.parseFireStations();
        jsonTools.parsePerson();
    }

    @Test
    public void addFireStationTest() {
        FireStation input = new FireStation();
        input.setStation(8);
        input.setAddress("Avenue de la République");
        JSONObject result = fireStationsService.addFireStation(input);
        assertFalse(result.isEmpty());
        assertEquals(result.get("station"), 8);
        assertEquals(result.get("address"), "Avenue de la République");
    }

    @Test
    public void addFireStationInvalidInputWithoutAddressTest() {
        FireStation input = new FireStation();
        input.setStation(8);
        assertThrows(InvalidInputException.class, () -> fireStationsService.addFireStation(input));
    }

    @Test
    public void addFireStationInvalidInputWithoutStationTest() {
        FireStation input = new FireStation();
        input.setAddress("Avenue de la République");
        assertThrows(InvalidInputException.class, () -> fireStationsService.addFireStation(input));
    }

    @Test
    public void addFireStationAlreadyExistTest() {
        FireStation input = new FireStation();
        input.setStation(24);
        input.setAddress("Boulevard Strasbourg");
        fireStationsService.addFireStation(input);
        assertThrows(AlreadyExistException.class, () -> fireStationsService.addFireStation(input));
    }

    @Test
    public void alreadyExistWithStationTest() {
        FireStation input = new FireStation();
        input.setStation(1);
        input.setAddress("Unknown");
        assertFalse(fireStationsService.alreadyExist(input));
    }

    @Test
    public void alreadyExistWithAddressTest() {
        FireStation input = new FireStation();
        input.setStation(452);
        input.setAddress("1509 Culver St");
        assertFalse(fireStationsService.alreadyExist(input));
    }

    @Test
    public void modifyFireStationTest() throws ParseException {
        FireStation input = new FireStation();
        input.setStation(8);
        input.setAddress("1509 Culver St");
        JSONObject result = fireStationsService.modifyFireStation(input);
        assertFalse(result.isEmpty());
        assertEquals(result.get("address"), input.getAddress());
        assertEquals(result.get("station").toString(), input.getStation().toString());
    }

    @Test
    public void modifyFireStationInvalidInputWithoutStationTest() {
        FireStation input = new FireStation();
        input.setStation(null);
        input.setAddress("1509 Culver St");
        assertThrows(InvalidInputException.class, () -> fireStationsService.modifyFireStation(input));
    }

    @Test
    public void modifyFireStationInvalidInputWithoutAddressTest() {
        FireStation input = new FireStation();
        input.setStation(8);
        input.setAddress(null);
        assertThrows(InvalidInputException.class, () -> fireStationsService.modifyFireStation(input));
    }

    @Test
    public void modifyFireStationNotFoundTest() {
        FireStation input = new FireStation();
        input.setStation(128);
        input.setAddress("Unknown");
        assertThrows(NotFoundException.class, () -> fireStationsService.modifyFireStation(input));
    }

    @Test
    public void deleteFireStationTest() throws ParseException {
        JSONObject input = new JSONObject();
        input.put("station", 3);
        input.put("address", "1509 Culver St");
        JSONArray result = fireStationsService.deleteFireStation(input);
        assertFalse(result.isEmpty());
        assertEquals(result.size() , 1);
    }

    @Test
    public void deleteFireStationMappingByStationTest() throws ParseException {
        JSONObject input = new JSONObject();
        input.put("station", 3);
        JSONArray result = fireStationsService.deleteFireStation(input);
        assertFalse(result.isEmpty());
        assertTrue(result.size() > 1);
    }

    @Test
    public void deleteFireStationMappingByAddressTest() throws ParseException {
        JSONObject input = new JSONObject();
        input.put("address", "1509 Culver St");
        JSONArray result = fireStationsService.deleteFireStation(input);
        assertFalse(result.isEmpty());
        assertTrue(result.size() == 1);
    }

    @Test
    public void deleteFireStationNotFoundTest() {
        JSONObject input = new JSONObject();
        input.put("station", 78954);
        input.put("address", "Unknown");
        assertThrows(NotFoundException.class, () -> fireStationsService.deleteFireStation(input));
    }

    @Test
    public void getPersonByStationTest() {
        JSONObject result = fireStationsService.getPersonByStation(1);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getPersonByStationNotFoundTest() {
        assertThrows(NotFoundException.class, () -> fireStationsService.getPersonByStation(8));
    }

    @Test
    public void getPersonByAddressTest() {
        JSONObject result = fireStationsService.getPersonByAddress("1509 Culver St");
        assertFalse(result.isEmpty());
        assertEquals(result.get("station"), 3);
    }

    @Test
    public void getPersonByAddressNotFoundTest() {
        assertThrows(NotFoundException.class, () -> fireStationsService.getPersonByAddress("Unknown"));
    }

    @Test
    public void getPhoneAlertTest() {
        JSONArray result = fireStationsService.getPhoneAlert(1);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getPhoneAlertNotFoundTest() {
        assertThrows(NotFoundException.class, () -> fireStationsService.getPhoneAlert(250));
    }

    @Test
    public void getFloodStationTest() {
        Integer[] stations =  new Integer[] {1,2,3,4};
        JSONObject response = fireStationsService.getFloodStation(stations);
        assertFalse(response.isEmpty());
    }

    @Test
    public void getFloodStationInvalidInputTest() {
        Integer[] stations =  new Integer[] {};
        assertThrows(InvalidInputException.class, () -> fireStationsService.getFloodStation(stations));
        assertThrows(InvalidInputException.class, () -> fireStationsService.getFloodStation(null));
    }

    @Test
    public void getFloodStationNotFoundTest() {
        Integer[] stations =  new Integer[] {874};
        assertThrows(NotFoundException.class, () -> fireStationsService.getFloodStation(stations));
    }
}
