package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.PersonsService;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Person Controller
 */
@RestController
public class PersonController {
    private final PersonsService personsService;
    private static Logger log = Logger.getLogger(PersonController.class);

    public PersonController(PersonsService personsService) {
        this.personsService = personsService;
    }

    /**
     * Add Person
     * @param person
     * @return
     */
    @PostMapping("/person")
    public ResponseEntity addPerson(@RequestBody Person person) throws Exception {
        log.debug("Ajouter une personne");
        JSONObject response;
        try {
            response = personsService.addPerson(person);
            log.info("La personne a été ajouté");

        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }  catch (AlreadyExistException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**
     * Modify Person
     * @param person
     * @return
     */
    @PutMapping("/person")
    public ResponseEntity modifyPerson(@RequestBody Person person) throws ParseException {
        log.debug("Modifier la personne");
        JSONObject response;
        try {
            response = personsService.modifyPerson(person);
            log.info("La personne a bien été modifier :" + response.toString());
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Delete Person
     * @param person
     * @return
     */
    @DeleteMapping("/person")
    public ResponseEntity deletePerson(@RequestBody Person person) {
        log.debug("Supprimer une personne : /person with body : " + person.toString());
        JSONObject response;
        try {
            response = personsService.deletePerson(person);
            log.info("La personne a bien été supprimé : " + person.getFirstName() + " " + person.getLastName());
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Get Person Info
     * @param firstName
     * @param lastName
     * @return
     */
    @GetMapping("/personInfo")
    @ResponseBody
    public ResponseEntity getPersonInfo(@RequestParam(required = false) String firstName, @RequestParam String lastName) {
        log.debug("GET information des personnes par nom");
        JSONArray response;
        try {
            response =  personsService.getPersonsInfo(firstName, lastName);
            log.info("Liste de personne trouvé au nom de " + lastName + " " + firstName);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Get Community Email
     * @param city
     * @return
     */
    @GetMapping("/communityEmail")
    public ResponseEntity getCommunityEmail(@RequestParam String city) {
        log.debug("Obtention d'une liste d'email de tout les habitants d'une ville");
        JSONArray response;
        try {
            response = personsService.getCommunityEmail(city);
            log.info("Liste d'email trouvé pour la ville : " + city);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Get Child alert
     * @param address
     * @return
     */
    @GetMapping("/childAlert")
    public ResponseEntity getChildAlert(@RequestParam String address) {
        log.debug("Obtention de la liste des enfants habitants à l'adresse.");
        JSONObject response;
        try {
            response = personsService.getChildByAddress(address);
            log.info("Résultat trouvé pour /childAlert");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
