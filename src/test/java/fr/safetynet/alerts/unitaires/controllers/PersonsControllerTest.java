package fr.safetynet.alerts.unitaires.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.controllers.PersonController;
import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.PersonsService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PersonController.class)
public class PersonsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonsService personsService;

    @Test
    public void testGetPersonInfoIsOk() throws Exception {
        mockMvc.perform(get("/personInfo?lastName=Boyd")).andExpect(status().isOk());
    }

    @Test
    public void testGetPersonInfoIsInvalid() throws Exception {
        mockMvc.perform(get("/personInfo")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/personInfo?firstName=Blabla")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPersonInfoNotFound() throws Exception {
        Mockito.when(personsService.getPersonsInfo(null, "Blabla")).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/personInfo?lastName=Blabla")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommunityEmail() throws Exception {
        mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk());
    }

    @Test
    public void testGetCommunityEmailNotFound() throws Exception {
        Mockito.when(personsService.getCommunityEmail( Mockito.any(String.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/communityEmail?city=Unknown")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommunityEmailInvalidInput() throws Exception {
        Mockito.when(personsService.getCommunityEmail( Mockito.any(String.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(get("/communityEmail?city=135")).andExpect(status().isBadRequest());
    }

    @Test
    public void testChildAlert() throws Exception {
        mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isOk());
    }

    @Test
    public void testChildNotFoundAlert() throws Exception {
        Mockito.when(personsService.getChildByAddress(Mockito.any(String.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/childAlert?address=Unknown")).andExpect(status().isNotFound());
    }

    @Test
    public void testAddPerson() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(post("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void testAddPersonInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName",null);
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.addPerson(Mockito.any(Person.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(post("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testAddPersonAlreadyExist() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.addPerson(Mockito.any(Person.class))).thenThrow(AlreadyExistException.class);
        mockMvc.perform(post("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyPerson() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.modifyPerson(Mockito.any(Person.class))).thenReturn(input);
        mockMvc.perform(put("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testModifyPersonInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.modifyPerson(Mockito.any(Person.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(put("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyPersonNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.modifyPerson(Mockito.any(Person.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(put("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePerson() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(delete("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testDeletePersonInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.deletePerson(Mockito.any(Person.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(delete("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testDeletePersonNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        Mockito.when(personsService.deletePerson(Mockito.any(Person.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(delete("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
