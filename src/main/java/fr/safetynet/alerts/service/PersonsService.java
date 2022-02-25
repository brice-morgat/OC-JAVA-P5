package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.InvalidStudentException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.PersonsRepo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PersonsService {
    public JSONObject addPerson(Person person) throws ParseException {
        JSONObject personResult = new JSONObject();
        //Parser le person pour vérifier que les champs soit valide
        if (person.firstName != null && person.lastName != null && person.address != null && person.city != null && person.zip != null && person.phone != null && person.email != null) {
            Person result = PersonsRepo.addPersons(person);
            JSONParser parser = new JSONParser();
            personResult = (JSONObject) parser.parse(JsonStream.serialize(result));
        } else {
            
        }
        return personResult;
    }

    public JSONObject modifyPerson(Person person) throws ParseException {
        JSONObject personResult = new JSONObject();
        Person result = PersonsRepo.modifyPerson(person);
        if (result != null) {
            JSONParser parser = new JSONParser();
            personResult = (JSONObject) parser.parse(JsonStream.serialize(result));
        }
        return personResult;
    }

    public JSONObject deletePerson(Person person) {
        JSONObject response = new JSONObject();
        if (person.firstName != null && person.lastName != null) {
            Person result =  PersonsRepo.deletePerson(person);
            response.put("firstName", result.firstName);
            response.put("lastName", result.lastName);
            return response;
        }
        return response;
    }

    public Person getPersonByName(String firstName, String lastName) {
        return PersonsRepo.getPersonByName(firstName, lastName);
    }
}
