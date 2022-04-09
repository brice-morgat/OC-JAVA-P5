package fr.safetynet.alerts.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.service.FireStationsService;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
import fr.safetynet.alerts.tools.JsonTools;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JsonTools jsonTools;

    @BeforeEach
    void prepareTest() {
        jsonTools = new JsonTools(new FireStationsService(), new MedicalRecordsService(), new PersonsService());
        jsonTools.parseFireStations();
        jsonTools.parseMedicalRecords();
        jsonTools.parsePerson();
    }

    @Test
    public void testGetPersonInfoIsOk() throws Exception {
        mockMvc.perform(get("/personInfo?lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.[0].*", hasSize(6)))
                .andExpect(jsonPath("$.[0].age", is(37)));

    }

    @Test
    public void testGetPersonInfoIsInvalid() throws Exception {
        mockMvc.perform(get("/personInfo")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/personInfo?firstName=Blabla")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPersonInfoNotFound() throws Exception {
        mockMvc.perform(get("/personInfo?lastName=Blabla")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommunityEmail() throws Exception {
        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(15)));
    }

    @Test
    public void testGetCommunityEmailNotFound() throws Exception {
        mockMvc.perform(get("/communityEmail?city=Unknown")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommunityEmailInvalidInput() throws Exception {
        mockMvc.perform(get("/communityEmail")).andExpect(status().isBadRequest());
    }

    @Test
    public void testChildAlert() throws Exception {
        mockMvc.perform(get("/childAlert?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.children", hasSize(2)))
                .andExpect(jsonPath("$.other", hasSize(3)))
                .andExpect(jsonPath("$.children.[0].firstName", is("Tenley")));
    }

    @Test
    public void testChildNotFoundAlert() throws Exception {
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
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(jsonPath("$.firstName", is("Prenom")));
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
        mockMvc.perform(post("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testAddPersonAlreadyExist() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(post("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyPerson() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(put("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    public void testModifyPersonInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", null);
        input.put("lastName","Nom");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(put("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyPersonNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "Johkjhn");
        input.put("lastName","Boyj");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(put("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePerson() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", "0787475144");
        input.put("zip", 232232);
        input.put("city", "Culver");
        mockMvc.perform(delete("/person").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    public void testDeletePersonInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("firstName", null);
        input.put("lastName","Boyd");
        input.put("address","address");
        input.put("email", "email@email.com");
        input.put("phone", null);
        input.put("zip", 232232);
        input.put("city", "Culver");
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
