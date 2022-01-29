package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.tools.JsonTools;

import java.util.ArrayList;
import java.util.List;

public class PersonsRepo {
    public static List<Person> persons = new ArrayList<>();

    public static void addPersons(Person person) {
        persons.add(person);
    }

    public static void removePerson(String firstName, String lastName) {
        for (Person person : persons) {
            if (person.getFirstName() == firstName && person.getLastName() == lastName) {
                persons.remove(person);
                return;
            }
        }
    }

    public static void modifyPerson(Person person) {
        int i = 0;
        for (Person personEntity : persons) {
            if (personEntity.getFirstName() == person.getFirstName() && personEntity.getLastName() == person.getLastName()) {
                persons.set(i, person);
                return;
            }
            i++;
        }
    }

    public static Person getPersonByName(String firstName, String lastName) {
        Person personToSearch = new Person();
        personToSearch.setFirstName(firstName);
        personToSearch.setLastName(lastName);
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return person;
            }
        }
        return personToSearch;
    }
}