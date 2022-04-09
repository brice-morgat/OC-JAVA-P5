package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.AlreadyExistException;
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

import java.util.ArrayList;
import java.util.List;

/**
 * FireStationService
 */
@Service
public class FireStationsService {
    FireStationsRepo fireStationsRepo = FireStationsRepo.getInstance();

    /**
     * Add a FireStation
     * @param fireStation
     * @return fireStation's removed
     */
    public JSONObject addFireStation(FireStation fireStation) {
        JSONObject fireStationResult = new JSONObject();
        if (fireStation.station != null && fireStation.address != null) {
            if (!alreadyExist(fireStation)) {
                FireStation result = fireStationsRepo.addFireStation(fireStation);
                fireStationResult.put("station", result.station);
                fireStationResult.put("address", result.address);
                return fireStationResult;
            } else {
                throw new AlreadyExistException("La station existe déjà pour");
            }
        } else {
            throw new InvalidInputException("Informations non valide");
        }
    }

    /**
     * Modify a FireStation mapping
     * @param fireStation
     * @return fireStation's modified
     */
    public JSONObject modifyFireStation(FireStation fireStation) throws ParseException {
        if (fireStation.station != null && fireStation.address != null) {
            FireStation result = fireStationsRepo.modifyFireStations(fireStation);
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

    /**
     * Delete FireStation
     * By address or by station or only one by address and station
     * @param request : JSONObject with station or address or station and address
     * @return fireStation's deleted
     */
    public JSONArray deleteFireStation(JSONObject request) throws ParseException {
        JSONArray response = new JSONArray();
        FireStation fireStationParam = new FireStation();

        if (request.get("station") != null) {
            fireStationParam.setStation(Integer.parseInt(request.get("station").toString()));
        }
        if (request.get("address") != null) {
            fireStationParam.setAddress(request.get("address").toString());
        }
        List<FireStation> result = fireStationsRepo.removeFireStation(fireStationParam);
        if (result != null) {
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

    /**
     * Get person by Station
     * @param station
     * @return list of all person covered by the station
     * and the number of child and adults of this list
     */
    public JSONObject getPersonByStation(int station) {
        JSONObject response = new JSONObject();
        JSONArray personsList = new JSONArray();
        int adults = 0;
        int child = 0;
        List addresses = fireStationsRepo.getListAddressByStationNumber(station);
        if (!addresses.isEmpty()) {
            List<Person> persons = PersonsRepo.getPersonsByAdresses(addresses);
            for (Person person: persons) {
                JSONObject personResult = new JSONObject();
                MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByNameAndFirstName(person.firstName, person.lastName);
                personResult.put("firstName", person.firstName);
                personResult.put("lastName", person.lastName);
                personResult.put("address", person.address);
                personResult.put("phone", person.phone);
                int age = 100;
                if (personMedicalRecord.getFirstName() != null) {
                    age = CalculTools.ageParser(personMedicalRecord.birthdate);
                }
                if (age >= 18) {
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
        } else {
            throw new NotFoundException("La station " + station + " n'existe pas.");
        }
    }

    /**
     * Get person by Address
     * @param address
     * @return a list of person who live at the address
     */
    public JSONObject getPersonByAddress(String address) {
        JSONObject response = new JSONObject();
        JSONArray persons = new JSONArray();
        int stationNumber = fireStationsRepo.getFireStationNumberByAddress(address);
        if (stationNumber != 0) {
            List<Person> personList = PersonsRepo.getPersonsByAddress(address);
            response.put("station", stationNumber);
            response.put("persons", persons);
            for (Person person : personList) {
                JSONObject entity = new JSONObject();
                MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByNameAndFirstName(person.firstName, person.lastName);
                entity.put("firstName", person.firstName);
                entity.put("lastName", person.lastName);
                entity.put("address", person.address);
                entity.put("phone", person.phone);
                int age = 100;
                if (personMedicalRecord.getFirstName() != null) {
                    age = CalculTools.ageParser(personMedicalRecord.birthdate);
                }
                entity.put("age", age);
                entity.put("medications", personMedicalRecord.medications);
                entity.put("allergies", personMedicalRecord.allergies);
                persons.add(entity);
            }
            return response;
        } else {
            throw new NotFoundException("Aucune station correspondante à l'adresse n'a été trouvé.");
        }
    }

    /**
     * Get phone alert by station
     * @param station
     * @return a list of phone number's persons who are covered by the station
     */
    public JSONArray getPhoneAlert(int station) {
        JSONArray phoneList = new JSONArray();
        List addresses = fireStationsRepo.getListAddressByStationNumber(station);
        if (!addresses.isEmpty()) {
            List<Person> persons = PersonsRepo.getPersonsByAdresses(addresses);
            for (Person person: persons) {
                if (!phoneList.contains(person.phone))
                    phoneList.add(person.phone);
            }
            return phoneList;
        } else {
            throw new NotFoundException("Aucune adresse trouvé correspondant à l'adresse.");
        }
    }

    /**
     * Get flood station
     * @param stations : a list of station number
     * @return a list of person for each address covered by a station in the list of stations
     */
    public JSONObject getFloodStation(Integer[] stations) {
        JSONObject response = new JSONObject();
        JSONArray personsList = new JSONArray();
        List<String> addresses = new ArrayList();
        if (stations != null) {
            for (int i : stations) {
                addresses.addAll(fireStationsRepo.getListAddressByStationNumber(i));
            }
            if (!addresses.isEmpty()) {
                for (String address : addresses) {
                    JSONArray personsArray = new JSONArray();
                    if (response.get(address) == null) {
                        List<Person> personList = PersonsRepo.getPersonsByAddress(address);
                        for (Person person : personList) {
                            JSONObject entity = new JSONObject();
                            MedicalRecord personMedicalRecord =  MedicalRecordsRepo.getMedicalRecordByNameAndFirstName(person.firstName, person.lastName);
                            entity.put("firstName", person.firstName);
                            entity.put("lastName", person.lastName);
                            entity.put("address", person.address);
                            entity.put("phone", person.phone);
                            int age = 100;
                            if (personMedicalRecord.getFirstName() != null) {
                                age = CalculTools.ageParser(personMedicalRecord.birthdate);
                            }
                            entity.put("age", age);
                            entity.put("medications", personMedicalRecord.medications);
                            entity.put("allergies", personMedicalRecord.allergies);
                            personsArray.add(entity);
                        }
                        response.put(address, personsArray);
                    }
                }
                return response;
            } else {
                if (stations.length == 0) {
                    throw new InvalidInputException("Aucune station saisie.");
                } else {
                    throw new NotFoundException("Aucune adresse trouvé correspondante à une de ces stations.");
                }
            }
        } else {
            throw new InvalidInputException("Station(s) saisie incorrecte.");
        }
    }

    /**
     * Verify if a fireStation already exist
     * @param fireStation
     * @return boolean
     */
    public boolean alreadyExist(FireStation fireStation) {
        int i = 0;
        for (FireStation entity : fireStationsRepo.getFireStations()) {
            if (entity.getAddress().equals(fireStation.address) && entity.getStation().equals(fireStation.station)) {
                return true;
            }
            i++;
        }
        return false;
    }
}