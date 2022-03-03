package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import fr.safetynet.alerts.repository.PersonsRepo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        if (person.firstName != null && person.lastName != null && person.address != null && person.city != null && person.zip != null && person.phone != null && person.email != null) {
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
                personResult.put("age", ageParser(personMedicalRecord.birthdate));
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
        if (city != null) {
            JSONArray response = new JSONArray();
            List<Person> persons = PersonsRepo.getPersonsByCity(city);
            if (!persons.isEmpty()) {
                for (Person person: persons) {
                    JSONObject personResult = new JSONObject();
                    personResult.put("email", person.email);
                    response.add(personResult);
                }
                return response;
            } else {
                throw new NotFoundException("Aucune personne ne correspond à la ville");
            }
        } else {
            throw new InvalidInputException("Aucune ville n'a été saisie");
        }
    }

    public JSONArray getChildByAddress(String address) {
        JSONArray result = new JSONArray();
        List<Person> persons = PersonsRepo.getPersonsByAddress(address);
        for(Person person : persons) {
            MedicalRecord personMedicalRecord = MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
            int age = ageParser(personMedicalRecord.birthdate);
            if (age <= 18) {
                JSONObject child = new JSONObject();
                child.put("firstName", person.firstName);
                child.put("lastName", person.lastName);
                child.put("age", age);
            }
        }
        return result;
    }

    private int ageParser(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate localDate = LocalDate.parse(birthdate, formatter);
            LocalDate now = LocalDate.now();
            Period period = Period.between(localDate, now);
            return period.getYears();
        } catch (Exception e) {
            throw 
        }
        return 0;
    }
}
