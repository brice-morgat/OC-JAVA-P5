package fr.safetynet.alerts.integrations;

import fr.safetynet.alerts.controllers.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;



@SpringBootTest
@AutoConfigureMockMvc
public class PersonServiceIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfo?lastName=Boyd")).andExpect(status().isOk()).andExpect(jsonPath("$[0].age", is(37)));
    }

    @Test
    public void testGetPersonInfoWithoutLastName() throws Exception {
        mockMvc.perform(get("/personInfo?firstName=Hello")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPersonInfoNotFound() throws Exception {
        mockMvc.perform(get("/personInfo?lastName=Blabla")).andExpect(status().isNotFound());
    }

}
