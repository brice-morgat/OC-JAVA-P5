package fr.safetynet.alerts.tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.repository.FireStationsRepo;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.FireStationsService;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
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
    PersonsRepo personsRepo = PersonsRepo.getInstance();
    FireStationsRepo fireStationsRepo = FireStationsRepo.getInstance();
    MedicalRecordsRepo medicalRecordsRepo = MedicalRecordsRepo.getInstance();

    private final FireStationsService fireStationsService;
    private final MedicalRecordsService medicalRecordsService;
    private final PersonsService personsService;

    public JsonTools(FireStationsService fireStationsService, MedicalRecordsService medicalRecordsService, PersonsService personsService) {
        this.fireStationsService = fireStationsService;
        this.medicalRecordsService = medicalRecordsService;
        this.personsService = personsService;
    }

    @PostConstruct
    private void init() {
        parsePerson();
        parseFireStations();
        parseMedicalRecords();
    }

    public void parsePerson() {
        personsRepo.clearPersons();
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
                if(!personsService.alreadyExist(entityPerson)) {
                    personsRepo.persons.add(entityPerson);
                }
            }
            System.out.println(personsRepo.persons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseFireStations() {
        fireStationsRepo.clearFireStations();
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
                if(!fireStationsService.alreadyExist(entityFireStation)) {
                    fireStationsRepo.getFireStations().add(entityFireStation);
                }
            }
           System.out.println(fireStationsRepo.getFireStations());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseMedicalRecords() {
        medicalRecordsRepo.clearMedicalRecords();
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
                if(!medicalRecordsService.alreadyExist(entityMedicalRecords)) {
                    medicalRecordsRepo.medicalRecords.add(entityMedicalRecords);
                }
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