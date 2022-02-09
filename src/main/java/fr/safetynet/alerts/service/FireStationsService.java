package fr.safetynet.alerts.service;

import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.repository.FireStationsRepo;
import fr.safetynet.alerts.repository.PersonsRepo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FireStationsService {
    public JSONObject addFireStation(FireStation fireStation) {
        JSONObject fireStationResult = new JSONObject();
        if (fireStation.station != null && fireStation.address != null) {
            FireStation result = FireStationsRepo.addFireStation(fireStation);
            fireStationResult.put("station", result.station);
            fireStationResult.put("address", result.address);
        }
        return fireStationResult;
    }

    public JSONObject modifyFireStation(FireStation fireStation) {
        JSONObject fireStationResult = new JSONObject();
        FireStation result = FireStationsRepo.modifyFireStations(fireStation);
        if (result != null) {
            fireStationResult.put("station", result.station);
            fireStationResult.put("address", result.address);
        }
        return fireStationResult;
    }

    public JSONObject deleteFireStation(JSONObject request) {
        JSONObject response = new JSONObject();
        FireStation fireStation = new FireStation();
        if (request.get("station") != null) {
            fireStation.setStation(Integer.parseInt(request.get("station").toString()));
        }
        if (request.get("address") != null) {
            fireStation.setAddress(request.get("address").toString());
        }
        FireStation result = FireStationsRepo.removeFireStation(fireStation);
        if (result != null) {
            response.put("station", result.station);
            response.put("address", result.address);
        }
        return response;
    }
}
