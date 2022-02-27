package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.PersonsRepo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class PersonsService {
    public JSONObject addPerson(Person person) throws ParseException {
        //Parser le person pour vérifier que les champs soit valide
        if (person.firstName != null && person.lastName != null && person.address != null && person.city != null && person.zip != null && person.phone != null && person.email != null) {
            Person result = PersonsRepo.addPersons(person);
            JSONParser parser = new JSONParser();
            JSONObject personResult = (JSONObject) parser.parse(JsonStream.serialize(result));
            return personResult;
        } else {
            throw new InvalidInputException("Personne non valide, il manque des informations");
        }
    }

    public JSONObject modifyPerson(Person person) throws ParseException {
        Person result = PersonsRepo.modifyPerson(person);

        if (result != null) {
            JSONParser parser = new JSONParser();
            JSONObject personResult = (JSONObject) parser.parse(JsonStream.serialize(result));
            return personResult;
        } else {
                throw new NotFoundException("Impossible de modifier cette personne");
        }
    }

    public JSONObject deletePerson(Person person) {
        JSONObject response = new JSONObject();
        if (person.firstName != null && person.lastName != null) {
            Person result =  PersonsRepo.deletePerson(person);
            if (result != null) {
                response.put("firstName", result.firstName);
                response.put("lastName", result.lastName);
                return response;
            } else  {
                throw new NotFoundException("La personne à supprimé n'existe pas ou est introuvable");
            }
        } else {
            throw new InvalidInputException("Nom ou prénom invalide");
        }
    }

    public Person getPersonByName(String firstName, String lastName) {
        return PersonsRepo.getPersonByName(firstName, lastName);
    }
}
