package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.FireStationsRepo;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.tools.CalculTools;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class FireStationsService {
    public JSONObject addFireStation(FireStation fireStation) {
        JSONObject fireStationResult = new JSONObject();
        if (fireStation.station != null && fireStation.address != null) {
            FireStation result = FireStationsRepo.addFireStation(fireStation);
            fireStationResult.put("station", result.station);
            fireStationResult.put("address", result.address);
            return fireStationResult;
        } else {
            throw new InvalidInputException("Informations non valide");
        }
    }

    public JSONObject modifyFireStation(FireStation fireStation) throws ParseException {
        if (fireStation.station != null || fireStation.address != null) {
            FireStation result = FireStationsRepo.modifyFireStations(fireStation);
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONObject fireStationResult = (JSONObject) parser.parse(JsonStream.serialize(result));
                return fireStationResult;
            } else {
                throw new NotFoundException("Station introuvable");
            }
        } else {
            throw new InvalidInputException("Information non valide");
        }
    }

    public JSONArray deleteFireStation(JSONObject request) throws ParseException {
        JSONArray response = new JSONArray();
        FireStation fireStationParam = new FireStation();

        if (request.get("station") != null) {
            fireStationParam.setStation(Integer.parseInt(request.get("station").toString()));
        }
        if (request.get("address") != null) {
            fireStationParam.setAddress(request.get("address").toString());
        }
        List<FireStation> result = FireStationsRepo.removeFireStation(fireStationParam);
        if (result != null && !result.isEmpty()) {
            for (FireStation firestation: result) {
                JSONParser parser = new JSONParser();
                JSONObject fireStationResult = (JSONObject) parser.parse(JsonStream.serialize(firestation));
                response.add(fireStationResult);
            }
            return response;
        } else {
            throw new NotFoundException("Aucune station trouvé");
        }
    }

    public JSONObject getPersonByStation(int station) {
        JSONObject response = new JSONObject();
        JSONArray personsList = new JSONArray();
        int adults = 0;
        int child = 0;
        int age = 0;
        List addresses = FireStationsRepo.getListAddressByStationNumber(station);
        List<Person> persons = PersonsRepo.getPersonsByAdresses(addresses);
        for (Person person: persons) {
            JSONObject personResult = new JSONObject();
            MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
            personResult.put("firstName", person.firstName);
            personResult.put("lastName", person.lastName);
            personResult.put("address", person.address);
            personResult.put("phone", person.phone);
            age = CalculTools.ageParser(personMedicalRecord.birthdate);
            if (age >= 18 || age == -1) {
                adults++;
            } else {
                child++;
            }
            personsList.add(personResult);
        }
        response.put("childs", child);
        response.put("adults", adults);
        response.put("persons", personsList);
        return response;
    }

    public JSONObject getPersonByAddress(String address) {
        JSONObject response = new JSONObject();
        JSONArray persons = new JSONArray();
        int stationNumber = FireStationsRepo.getFireStationNumberByAddress(address);
        List<Person> personList = PersonsRepo.getPersonsByAddress(address);
        response.put("station", stationNumber);
        response.put("persons", persons);
        for (Person person : personList) {
            JSONObject entity = new JSONObject();
            MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
            entity.put("firstName", person.firstName);
            entity.put("lastName", person.lastName);
            entity.put("address", person.address);
            entity.put("phone", person.phone);
            int age = CalculTools.ageParser(personMedicalRecord.birthdate);
            entity.put("age", age);
            entity.put("medications", personMedicalRecord.medications);
            entity.put("allergies", personMedicalRecord.allergies);
            persons.add(entity);
        }
        return response;
    }

    public JSONArray getPhoneAlert(int station) {
        JSONArray phoneList = new JSONArray();
        List addresses = FireStationsRepo.getListAddressByStationNumber(station);
        List<Person> persons = PersonsRepo.getPersonsByAdresses(addresses);
        for (Person person: persons) {
            if (!phoneList.contains(person.phone))
                phoneList.add(person.phone);
        }
        return phoneList;
    }

    public JSONObject getFloodStation(Integer[] station) {
        JSONObject response = new JSONObject();
        JSONArray personsList = new JSONArray();
        List<String> addresses = new ArrayList();
        for (int i : station) {
            addresses.addAll(FireStationsRepo.getListAddressByStationNumber(i));
        }

        for (String address : addresses) {
            JSONArray personsArray = new JSONArray();
            List<Person> personList = PersonsRepo.getPersonsByAddress(address.toString());
            for (Person person : personList) {
                JSONObject entity = new JSONObject();
                MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
                entity.put("firstName", person.firstName);
                entity.put("lastName", person.lastName);
                entity.put("address", person.address);
                entity.put("phone", person.phone);
                int age = CalculTools.ageParser(personMedicalRecord.birthdate);
                entity.put("age", age);
                entity.put("medications", personMedicalRecord.medications);
                entity.put("allergies", personMedicalRecord.allergies);
                personsArray.add(entity);
            }
            response.put(address, personsArray);
        }

        return response;
    }
}