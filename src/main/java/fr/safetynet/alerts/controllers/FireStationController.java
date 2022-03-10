package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.service.FireStationsService;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class FireStationController {
    private final FireStationsService fireStationsService;
    private static Logger log = Logger.getLogger(FireStationController.class);

    public FireStationController(FireStationsService fireStationsService) {
        this.fireStationsService = fireStationsService;
    }

    @PostMapping("/firestation")
    public ResponseEntity addFireStation(@RequestBody FireStation fireStation) {
        log.debug("Ajouter une station");
        JSONObject response;
        try {
            response = fireStationsService.addFireStation(fireStation);
            log.info("La station a bien été ajouté : " + response.toString());
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PutMapping("/firestation")
    public ResponseEntity modifyFireStation(@RequestBody FireStation fireStation) throws ParseException {
        log.debug("Modifier une station");
        JSONObject response;
        try {
            response = fireStationsService.modifyFireStation(fireStation);
            log.info("La station a bien été modifié : " + response.toString());
        } catch (InvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/firestation")
    public ResponseEntity deleteFireStation(@RequestBody JSONObject fireStation) throws ParseException {
        log.debug("Supprimer une station");
        JSONArray response;
        try {
            response =  fireStationsService.deleteFireStation(fireStation);
            log.info("La station a bien été supprimé");
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/firestation")
    public ResponseEntity getPersonByStation(@RequestParam int stationNumber) {
        log.info("Obtention de la liste des personnes couverte par une station");
        JSONObject response;
        try {
            response = fireStationsService.getPersonByStation(stationNumber);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/fire")
    public ResponseEntity fireAddress(@RequestParam String address) {
        JSONObject response;
        response = fireStationsService.getPersonByAddress(address);

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
