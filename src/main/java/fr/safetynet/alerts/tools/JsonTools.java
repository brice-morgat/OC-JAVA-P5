package fr.safetynet.alerts.tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.repository.FireStationsRepo;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class JsonTools {
    PersonsRepo personsRepo = new PersonsRepo();
    FireStationsRepo fireStationsRepo = new FireStationsRepo();
    MedicalRecordsRepo medicalRecordsRepo = new MedicalRecordsRepo();

    @PostConstruct
    private void parsePerson() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser
                    .parse(new FileReader("src/main/resources/data.json"));

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray persons = (JSONArray) jsonObject.get("persons");

            for (JSONObject person : (Iterable<JSONObject>) persons) {
                Person entityPerson = new Person();
                entityPerson.setFirstName(person.get("firstName").toString());
                entityPerson.setZip(Integer.parseInt(person.get("zip").toString()));
                entityPerson.setLastName(person.get("lastName").toString());
                entityPerson.setEmail(person.get("email").toString());
                entityPerson.setAddress(person.get("address").toString());
                entityPerson.setCity(person.get("city").toString());
                entityPerson.setPhone(person.get("phone").toString());
                personsRepo.persons.add(entityPerson);
            }
            System.out.println(personsRepo.persons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void parseFireStations() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser
                    .parse(new FileReader("src/main/resources/data.json"));

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray firestations = (JSONArray) jsonObject.get("firestations");

            Iterator<JSONObject> iterator = firestations.iterator();
            while (iterator.hasNext()) {
                JSONObject firestation = iterator.next();
                FireStation entityFireStation = new FireStation();
                entityFireStation.setAddress(firestation.get("address").toString());
                entityFireStation.setStation(Integer.parseInt(firestation.get("station").toString()));
                fireStationsRepo.fireStations.add(entityFireStation);
            }
           System.out.println(fireStationsRepo.fireStations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void parseMedicalRecords() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser
                    .parse(new FileReader("src/main/resources/data.json"));

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray medicalrecords = (JSONArray) jsonObject.get("medicalrecords");

            Iterator<JSONObject> iterator = medicalrecords.iterator();

            while (iterator.hasNext()) {
                JSONObject medicalrecord = iterator.next();
                MedicalRecord entityMedicalRecords = new MedicalRecord();
                entityMedicalRecords.setFirstName(medicalrecord.get("firstName").toString());
                entityMedicalRecords.setLastName(medicalrecord.get("lastName").toString());
                entityMedicalRecords.setBirthdate(medicalrecord.get("birthdate").toString());
                entityMedicalRecords.setAllergies(allergiesToList(medicalrecord.get("allergies").toString()));
                entityMedicalRecords.setMedications(medicationsToList(medicalrecord.get("medications").toString()));
                medicalRecordsRepo.medicalRecords.add(entityMedicalRecords);
            }
            System.out.println(medicalRecordsRepo.medicalRecords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> medicationsToList(String medications) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> result = mapper.readValue(medications.getBytes(), List.class);
        return result;
    }

    public List<String> allergiesToList(String allergies) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> result = mapper.readValue(allergies.getBytes(), List.class);
        return result;
    }
}