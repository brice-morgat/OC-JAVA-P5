package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.FireStationsRepo;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.tools.CalculTools;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PersonsService {
    private static Logger log = Logger.getLogger(PersonsService.class);

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
        if (person.firstName != null && person.lastName != null && person.address != null && person.city != null && person.zip != null && person.phone != null && person.email != null) {
            Person result = PersonsRepo.modifyPerson(person);
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONObject personResult = (JSONObject) parser.parse(JsonStream.serialize(result));
                return personResult;
            } else {
                throw new NotFoundException("Impossible de modifier cette personne");
            }
        } else {
            throw new InvalidInputException("Personne non valide, il manque des informations");
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

    public JSONArray getPersonsInfo(String firstName, String lastName) {
        JSONArray response = new JSONArray();
        List<Person> persons = PersonsRepo.getPersonsByName(firstName, lastName);
        if (!persons.isEmpty()) {
            for (Person person: persons) {
                JSONObject personResult = new JSONObject();
                MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
                personResult.put("lastName", person.lastName);
                personResult.put("address", person.address);
                personResult.put("age", CalculTools.ageParser(personMedicalRecord.birthdate));
                personResult.put("email", person.email);
                personResult.put("medications", personMedicalRecord.medications);
                response.add(personResult);
            }
            return response;
        } else {
            throw new NotFoundException("Aucune personne n'a été trouvé");
        }
    }

    public JSONArray getCommunityEmail(String city) {
        if (city != null && !city.equals("")) {
            JSONArray response = new JSONArray();
            List<Person> persons = PersonsRepo.getPersonsByCity(city);
            if (!persons.isEmpty()) {
                for (Person person: persons) {
                    if (!response.contains(person.email))
                        response.add(person.email);
                }
                return response;
            } else {
                throw new NotFoundException("Aucune personne ne correspond à la ville");
            }
        } else {
            throw new InvalidInputException("Aucune ville n'a été saisie");
        }
    }

    public JSONObject getChildByAddress(String address) {
        JSONObject response = new JSONObject();
        JSONArray persons = new JSONArray();
        JSONArray children = new JSONArray();
        int child = 0;

        List<Person> personList = PersonsRepo.getPersonsByAddress(address);

        for (Person person : personList) {
            JSONObject entity = new JSONObject();
            MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
            entity.put("firstName", person.firstName);
            entity.put("lastName", person.lastName);
            int age = CalculTools.ageParser(personMedicalRecord.birthdate);

            entity.put("age", age);
            if (age <= 18) {
                children.add(entity);
                child++;
            } else {
                persons.add(entity);
            }
        }
        if (child == 0) {
            return response;
        }

        response.put("other", persons);
        response.put("children", children);

        return response;
    }
}
