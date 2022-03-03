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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public JSONArray getPersonByStation(int station) {
        JSONArray response = new JSONArray();
        int adults = 0;
        int child = 0;
        List addresses = FireStationsRepo.getListAddressByStationNumber(station);
        List<Person> persons = PersonsRepo.getPersonsByAdresses(addresses);
        for (Person person: persons) {
            JSONObject personResult = new JSONObject();
            MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByName(person.firstName, person.lastName);
            personResult.put("firstName", person.firstName);
            personResult.put("lastName", person.lastName);
            personResult.put("address", person.address);
            personResult.put("phone", person.phone);
            if (ageParser(personMedicalRecord.birthdate) >= 18) {
                adults++;
            } else {
                child++;
            }
            response.add(personResult);
        }
        JSONObject counting = new JSONObject();
        counting.put("childs", child);
        counting.put("adults", adults);
        response.add(counting);
        return response;
    }

    private int ageParser(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate localDate = LocalDate.parse(birthdate, formatter);
            LocalDate now = LocalDate.now();
            Period period = Period.between(localDate, now);
            return period.getYears();
        } catch (Exception e) {

        }
        return 0;
    }
}