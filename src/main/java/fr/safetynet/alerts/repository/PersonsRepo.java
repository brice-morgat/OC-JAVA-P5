package fr.safetynet.alerts.repository;
import fr.safetynet.alerts.models.Person;


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
            if (personEntity.getFirstName().equals(person.getFirstName()) && personEntity.getLastName().equals(person.getLastName())) {
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

    public static List<Person> getPersonsByCity(String city) {
        List<Person> result = new ArrayList();
        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                result.add(person);
            }
        }
        return result;
    }

    public static List<Person> getPersonsByName(String firstName, String lastName) {
        List<Person> result = new ArrayList();
        if (firstName != null) {
            for (Person person : persons) {
                if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                    result.add(person);
                }
            }
        } else {
            for (Person person : persons) {
                if (person.getLastName().equals(lastName)) {
                    result.add(person);
                }
            }
        }
        return result;
    }

    public static List<Person> getPersonsByAdresses(List addresses) {
        List<Person> result = new ArrayList();
        for (Person person : persons) {
            if (addresses.contains(person.getAddress())) {
                result.add(person);
            }
        }
        return result;
    }

    public static List<Person> getPersonsByAddress(String address) {
        List<Person> result = new ArrayList();
        for (Person person : persons) {
            if (person.getAddress().equals(address)) {
                result.add(person);
            }
        }
        return result;
    }
}