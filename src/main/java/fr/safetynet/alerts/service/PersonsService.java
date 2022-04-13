package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.AlreadyExistException;
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

import java.util.List;

@Service
public class PersonsService {
    PersonsRepo personsRepo = PersonsRepo.getInstance();
    MedicalRecordsRepo medicalRecordsRepo = MedicalRecordsRepo.getInstance();

    /**
     * Add Person
     * @param person
     * @return person added
     */
    public JSONObject addPerson(Person person) throws ParseException {
        //Parser le person pour vérifier que les champs soit valide
        if (person.getFirstName() != null && person.getLastName() != null && person.getAddress() != null && person.getCity() != null && person.getZip() != null && person.getPhone() != null && person.getEmail() != null) {
            if(!alreadyExist(person)) {
                Person result = personsRepo.addPersons(person);
                JSONParser parser = new JSONParser();
                JSONObject personResult = (JSONObject) parser.parse(JsonStream.serialize(result));
                return personResult;
            } else {
                throw new AlreadyExistException("Cette personne existe déjà." );
             }
        } else {
            throw new InvalidInputException("Personne non valide, il manque des informations");
        }
    }

    /**
     * Modify Person
     * @param person
     * @return person modified
     */
    public JSONObject modifyPerson(Person person) throws ParseException {
        if (person.getFirstName() != null && person.getLastName() != null && person.getAddress() != null && person.getCity() != null && person.getZip() != null && person.getPhone() != null && person.getEmail() != null) {
            Person result = personsRepo.modifyPerson(person);
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

    /**
     * Delete a person
     * @param person
     * @return person deleted
     */
    public JSONObject deletePerson(Person person) {
        JSONObject response = new JSONObject();
        if (person.getFirstName() != null && person.getLastName() != null) {
            Person result =  personsRepo.deletePerson(person);
            if (result != null) {
                response.put("firstName", result.getFirstName());
                response.put("lastName", result.getLastName());
                return response;
            } else  {
                throw new NotFoundException("La personne à supprimé n'existe pas ou est introuvable");
            }
        } else {
            throw new InvalidInputException("Nom ou prénom invalide");
        }
    }

    /**
     * Get Person info
     * @param firstName
     * @param lastName
     * @return list of person by lastname or person by firstname and lastname
     */
    public JSONArray getPersonsInfo(String firstName, String lastName) {
        JSONArray response = new JSONArray();
        List<Person> persons = personsRepo.getPersonsByName(firstName, lastName);
        if (!persons.isEmpty()) {
            for (Person person: persons) {
                JSONObject personResult = new JSONObject();
                MedicalRecord personMedicalRecord =  medicalRecordsRepo.getMedicalRecordByNameAndFirstName(person.getFirstName(), person.getLastName());
                personResult.put("lastName", person.getLastName());
                personResult.put("address", person.getAddress());
                int age = 100;
                if (personMedicalRecord.getFirstName() != null) {
                    age = CalculTools.ageParser(personMedicalRecord.getBirthdate());
                }
                personResult.put("age", age);
                personResult.put("email", person.getEmail());
                personResult.put("medications", personMedicalRecord.getMedications());
                personResult.put("allergies", personMedicalRecord.getAllergies());
                response.add(personResult);
            }
            return response;
        } else {
            throw new NotFoundException("Aucune personne n'a été trouvé");
        }
    }

    /**
     * Get Community Email
     * @param city
     * @return Email list of person living in the city
     */
    public JSONArray getCommunityEmail(String city) {
        if (city != null && !city.equals("")) {
            JSONArray response = new JSONArray();
            List<Person> persons = personsRepo.getPersonsByCity(city);
            if (!persons.isEmpty()) {
                for (Person person: persons) {
                    if (!response.contains(person.getEmail()))
                        response.add(person.getEmail());
                }
                return response;
            } else {
                throw new NotFoundException("Aucune personne ne correspond à la ville");
            }
        } else {
            throw new InvalidInputException("Aucune ville n'a été saisie");
        }
    }

    /**
     * Get Child By Address
     * @param address
     * @return if there are child(ren), return a list of child living at the address, else return empty
     */
    public JSONObject getChildByAddress(String address) {
        JSONObject response = new JSONObject();
        JSONArray persons = new JSONArray();
        JSONArray children = new JSONArray();
        int child = 0;

        List<Person> personList = personsRepo.getPersonsByAddress(address);
        if (!personList.isEmpty()) {
            for (Person person : personList) {
                JSONObject entity = new JSONObject();
                MedicalRecord personMedicalRecord =  medicalRecordsRepo.getMedicalRecordByNameAndFirstName(person.getFirstName(), person.getLastName());
                entity.put("firstName", person.getFirstName());
                entity.put("lastName", person.getLastName());
                int age = 100;
                if (personMedicalRecord.getFirstName() != null) {
                    age = CalculTools.ageParser(personMedicalRecord.getBirthdate());
                }
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
        } else {
            throw new NotFoundException("Aucune personne n'a été trouvé.");
        }
    }

    /**
     * Verify if a person already exist
     * @param person
     * @return boolean
     */
    public boolean alreadyExist(Person person) {
        int i = 0;
        for (Person entity : PersonsRepo.persons) {
            if (entity.getFirstName().equals(person.getFirstName()) && entity.getLastName().equals(person.getLastName())) {
                return true;
            }
            i++;
        }
        return false;
    }
}
