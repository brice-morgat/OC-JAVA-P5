package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.exceptions.AlreadyExistException;
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

/**
 * FireStation Controller
 */
@RestController
public class FireStationController {
    private final FireStationsService fireStationsService;
    private static final Logger log = Logger.getLogger(FireStationController.class);

    public FireStationController(FireStationsService fireStationsService) {
        this.fireStationsService = fireStationsService;
    }

    /**
     * Add FireStation
     * @param fireStation
     * @return
     */
    @PostMapping("/firestation")
    public ResponseEntity addFireStation(@RequestBody FireStation fireStation) {
        log.debug("Ajouter une station");
        JSONObject response;
        try {
            response = fireStationsService.addFireStation(fireStation);
            log.info("La station a bien été ajouter");
        } catch (InvalidInputException | AlreadyExistException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**
     * Modify FireStation
     * @param fireStation
     * @return
     */
    @PutMapping("/firestation")
    public ResponseEntity modifyFireStation(@RequestBody FireStation fireStation) throws ParseException {
        log.debug("Modifier une station");
        JSONObject response;
        try {
            response = fireStationsService.modifyFireStation(fireStation);
            log.info("La station a bien été modifier" );
        } catch (InvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     *
     * @param fireStation
     * @return
     */
    @DeleteMapping("/firestation")
    public ResponseEntity deleteFireStation(@RequestBody JSONObject fireStation) throws ParseException {
        log.debug("Supprimer une station");
        JSONArray response;
        try {
            response =  fireStationsService.deleteFireStation(fireStation);
            log.info("La station a bien été supprimer");
        } catch (InvalidInputException e ) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Get Person By Station
     * @param stationNumber
     * @return
     */
    @GetMapping("/firestation")
    public ResponseEntity getPersonByStation(@RequestParam int stationNumber) {
        log.debug("Obtention de la liste des personnes couverte par une station");
        JSONObject response;
        try {
            response = fireStationsService.getPersonByStation(stationNumber);
            log.info("Liste de personnes couverte par la station obtenu.");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Get list of persons living at the address
     * @param address
     * @return
     */
    @GetMapping("/fire")
    public ResponseEntity fireAddress(@RequestParam String address) {
        log.debug("Obtention de la liste des personnes vivant à l'adresse indiqué.");
        JSONObject response;
        try {
            response = fireStationsService.getPersonByAddress(address);
            log.info("Liste trouvé pour /fire?address=");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Phone Alert
     * @param firestation
     * @return
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity phoneAlert(@RequestParam int firestation) {
        log.debug("Obtention de la liste des numéros téléphone couvert par la station indiqué.");
        JSONArray response;
        try {
            response = fireStationsService.getPhoneAlert(firestation);
            log.info("Liste trouvé pour /phoneAlert?firestation=");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Flood Stations
     * Get list of all person covered by the station and the number of child and adults of this list
     * @param stations
     * @return
     */
    @GetMapping("/flood/stations")
    public ResponseEntity floodStations(@RequestParam Integer[] stations) {
        log.debug("Obtention de la liste des membres de chaque foyers desservies par une caserne donnée.");
        JSONObject response;
        try {
            response = fireStationsService.getFloodStation(stations);
            log.info("Listes des foyers obtenues pour /flood/stations?stations");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
