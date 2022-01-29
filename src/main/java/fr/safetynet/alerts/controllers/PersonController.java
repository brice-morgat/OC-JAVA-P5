package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.models.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @GetMapping("/personInfo")
    @ResponseBody
    public Person getPerson(@RequestParam String firstName, @RequestParam String lastName) {
        Person personToSearch = new Person();
        personToSearch.setFirstName(firstName);
        personToSearch.setLastName(lastName);

        return PersonsRepo.getPersonByName(firstName, lastName);
    }
}
