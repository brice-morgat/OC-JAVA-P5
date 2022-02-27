package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.PersonsService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PersonController {
    private final PersonsService personsService;

    public PersonController(PersonsService personsService) {
        this.personsService = personsService;
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public JSONObject addPerson(@RequestBody Person person) throws Exception {
        try {
            return personsService.addPerson(person);
        } catch (InvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public JSONObject modifyPerson(@RequestBody Person person) throws ParseException {
        try {
            return personsService.modifyPerson(person);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public JSONObject deletePerson(@RequestBody Person person) {
        try {
            return personsService.deletePerson(person);
        } catch (InvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/personInfo")
    @ResponseBody
    public Person getPerson(@RequestParam String firstName, @RequestParam String lastName) {
        return personsService.getPersonByName(firstName, lastName);
    }
}
