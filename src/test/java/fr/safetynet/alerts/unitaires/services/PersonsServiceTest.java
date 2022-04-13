package fr.safetynet.alerts.unitaires.services;

import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.FireStationsService;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
import fr.safetynet.alerts.tools.JsonTools;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonsServiceTest {

    @Autowired
    private PersonsService personsService;

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
    public void getPersonInfoTest() {
        JSONArray persons = personsService.getPersonsInfo("John", "Boyd");
        assertTrue(persons != null);
    }

    @Test
    public void getPersonInfoOnlyLastNameTest() {
        JSONArray persons = personsService.getPersonsInfo(null, "Boyd");
        assertTrue(persons != null);
    }

    @Test
    public void getPersonInfoNotFoundTest() {
        assertThrows(NotFoundException.class, () -> personsService.getPersonsInfo(null, ""));
    }

    @Test
    public void addPersonTest() throws ParseException {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        JSONObject result = personsService.addPerson(person);
        assertEquals(result.get("firstName"), person.getFirstName());
        assertEquals(result.get("lastName"), person.getLastName());
    }

    @Test
    public void addPersonAlreadyExistTest() throws ParseException {
        Person person = new Person();
        person.setFirstName("Already");
        person.setLastName("Exist");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        JSONObject result = personsService.addPerson(person);
        assertEquals(result.get("firstName"), person.getFirstName());
        assertEquals(result.get("lastName"), person.getLastName());
        assertThrows(AlreadyExistException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void alreadyExistWithFirstNameTest() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Unknown");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        assertFalse(personsService.alreadyExist(person));
    }

    @Test
    public void alreadyExistWithLastNameTest() {
        Person person = new Person();
        person.setFirstName("Unknown");
        person.setLastName("Boyd");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        assertFalse(personsService.alreadyExist(person));
    }


    @Test
    public void addPersonWithoutFirstNameTest() {
        Person person = new Person();
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void addPersonWithoutLastNameTest() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void addPersonWithoutAddressTest() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void addPersonWithoutEmailTest() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void addPersonWithoutPhoneTest() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setZip(232232);
        person.setCity("Culver");
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void addPersonWithoutZipTest() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setCity("Culver");
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void addPersonWithoutCityTest() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }

    @Test
    public void deletePersonTest() {
        Person personToDelete = new Person();
        personToDelete.setLastName("Boyd");
        personToDelete.setFirstName("John");
        JSONObject result = personsService.deletePerson(personToDelete);
        assertEquals(result.get("firstName"), personToDelete.getFirstName());
        assertEquals(result.get("lastName"), personToDelete.getLastName());
        assertFalse(result.isEmpty());
    }

    @Test
    public void deletePersonNotFoundTest() {
        Person personToDelete = new Person();
        personToDelete.setLastName("Inconnu");
        personToDelete.setFirstName("Inconnu");
        assertThrows(NotFoundException.class, () -> personsService.deletePerson(personToDelete));
    }

    @Test
    public void deletePersonInvalidInputTest() {
        Person personToDeleteWithoutLastName = new Person();
        Person personToDeleteWithoutFirstName = new Person();
        personToDeleteWithoutFirstName.setLastName("Inconnu");
        personToDeleteWithoutFirstName.setFirstName(null);
        personToDeleteWithoutLastName.setLastName(null);
        personToDeleteWithoutLastName.setFirstName("Inconnu");
        assertThrows(InvalidInputException.class, () -> personsService.deletePerson(personToDeleteWithoutFirstName));
        assertThrows(InvalidInputException.class, () -> personsService.deletePerson(personToDeleteWithoutLastName));
    }

    @Test
    public void modifyPersonTest() throws ParseException {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setFirstName("John");
        personToModify.setLastName("Boyd");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        JSONObject result = personsService.modifyPerson(personToModify);
        assertEquals(result.get("firstName"), "John");
        assertEquals(result.get("lastName"), "Boyd");
        assertEquals(result.get("city"), "Toulon");
        assertEquals(result.get("zip").toString(), "83000");
    }

    @Test
    public void modifyPersonWithoutFirstNameTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setLastName("Boyd");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonWithoutLastNameTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setFirstName("John");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonWithoutAddressTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setLastName("Boyd");
        personToModify.setFirstName("John");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonWithoutCityTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setLastName("Boyd");
        personToModify.setFirstName("John");
        personToModify.setAddress("1509 Culver St");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonWithoutZipTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setLastName("Boyd");
        personToModify.setFirstName("John");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonWithoutPhoneTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setLastName("Boyd");
        personToModify.setFirstName("John");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonWithoutEmailTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setFirstName("John");
        personToModify.setLastName("Boyd");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");

        assertThrows(InvalidInputException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void modifyPersonNotFoundTest() {
        //{ "firstName":"John", "lastName":"Boyd", "address":"1509 Culver St", "city":"Culver", "zip":"97451", "phone":"841-874-6512", "email":"jaboyd@email.com" },
        Person personToModify = new Person();
        personToModify.setFirstName("Unknown");
        personToModify.setLastName("Unknown");
        personToModify.setAddress("1509 Culver St");
        personToModify.setCity("Toulon");
        personToModify.setZip(83000);
        personToModify.setPhone("841-874-6512");
        personToModify.setEmail("jayboyd@email.com");

        assertThrows(NotFoundException.class, () -> personsService.modifyPerson(personToModify));
    }

    @Test
    public void getCommunityEmailTest() {
        JSONArray result = personsService.getCommunityEmail("Culver");
        assertEquals(result.isEmpty(), false);
    }

    @Test
    public void getCommunityEmailInvalidInputTest() {
        assertThrows(InvalidInputException.class, () -> personsService.getCommunityEmail(null));
        assertThrows(InvalidInputException.class, () -> personsService.getCommunityEmail(""));
    }

    @Test
    public void getCommunityEmailNotFoundTest() {
        assertThrows(NotFoundException.class, () -> personsService.getCommunityEmail("jflk,nkjlh"));
    }

    @Test
    public void getChildByAddressTest() {
        JSONObject result = personsService.getChildByAddress("1509 Culver St");
        assertTrue(!result.isEmpty());
        assertTrue(result.get("children") != null);
        assertTrue(result.get("other") != null);
    }

    @Test
    public void getChildByAddressNoChildTest() throws ParseException {
        Person adult = new Person();
        adult.setLastName("Nom");
        adult.setFirstName("Prénom");
        adult.setAddress("Avenue général");
        adult.setCity("Toulon");
        adult.setZip(83000);
        adult.setPhone("841-874-6512");
        adult.setEmail("jayboyd@email.com");
        personsService.addPerson(adult);
        JSONObject result = personsService.getChildByAddress("Avenue général");
        assertTrue(result.isEmpty());
    }

    @Test
    public void getChildByAddressNotFoundTest() throws ParseException {
        assertThrows(NotFoundException.class, () -> personsService.getChildByAddress("Unknown"));
    }
}
