package fr.safetynet.alerts.unitaires.controllers;

import fr.safetynet.alerts.controllers.PersonController;
import fr.safetynet.alerts.service.PersonsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}
