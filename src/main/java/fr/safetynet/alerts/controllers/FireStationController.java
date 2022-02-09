package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.service.FireStationsService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class FireStationController {

    private final FireStationsService fireStationsService;

    public FireStationController(FireStationsService fireStationsService) {
        this.fireStationsService = fireStationsService;
    }

    @PostMapping("/firestation")
    @ResponseStatus(HttpStatus.CREATED)
    public JSONObject addFireStation(@RequestBody FireStation fireStation) {
        return fireStationsService.addFireStation(fireStation);
    }

    @PutMapping("/firestation")
    @ResponseStatus(HttpStatus.OK)
    public JSONObject modifyFireStation(@RequestBody FireStation fireStation) {
        return fireStationsService.modifyFireStation(fireStation);
    }

    @DeleteMapping("/firestation")
    @ResponseStatus(HttpStatus.OK)
    public JSONObject deleteFireStation(@RequestBody JSONObject fireStation) {
        return fireStationsService.deleteFireStation(fireStation);
    }
}
