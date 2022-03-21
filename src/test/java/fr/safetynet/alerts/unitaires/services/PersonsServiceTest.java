package fr.safetynet.alerts.unitaires.services;

import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.service.PersonsService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonsServiceTest {

    @Mock
    PersonsRepo personsRepo;

    @Autowired
    private PersonsService personsService;

    @Test
    public void getPersonInfo() {
        JSONArray persons = personsService.getPersonsInfo(null, "Boyd");
        assertTrue(persons != null);
    }

    @Test
    public void getPersonInfoNotFound() {
        assertThrows(NotFoundException.class, () -> personsService.getPersonsInfo(null, ""));
    }

    @Test
    public void addPerson() throws ParseException {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        person.setCity("Culver");
        JSONObject result = personsService.addPerson(person);
        assertEquals(result.get("firstName"), person.firstName);
        assertEquals(result.get("lastName"), person.lastName);
    }

    @Test
    public void addPersonWithoutFirstName() {
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
    public void addPersonWithoutLastName() {
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
    public void addPersonWithoutAddress() {
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
    public void addPersonWithoutEmail() {
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
    public void addPersonWithoutPhone() {
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
    public void addPersonWithoutZip() {
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
    public void addPersonWithoutCity() {
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setEmail("email@email.com");
        person.setPhone("0787475144");
        person.setZip(232232);
        assertThrows(InvalidInputException.class, () -> personsService.addPerson(person));
    }
}
