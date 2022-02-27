package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.tools.JsonTools;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class PersonsRepo {
    public static List<Person> persons = new ArrayList<>();

    public static Person addPersons(Person person) {
        persons.add(person);
        return person;
    }

    public static Person modifyPerson(Person person) {
        int i = 0;
        for (Person personEntity : persons) {
            if (personEntity.getFirstName() == person.getFirstName() && personEntity.getLastName() == person.getLastName()) {
                persons.set(i, person);
                return person;
            }
            i++;
        }
        return null;
    }

    public static Person deletePerson(Person person) {
        int i = 0;
        for (Person personEntity : persons) {
            if (personEntity.getFirstName().equals(person.firstName) && personEntity.getLastName().equals(person.lastName)) {
                persons.remove(i);
                return person;
            }
            i++;
        }
        return null;
    }

    public static Person getPersonByName(String firstName, String lastName) {

        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return person;
            }
        }
        return null;
    }
}