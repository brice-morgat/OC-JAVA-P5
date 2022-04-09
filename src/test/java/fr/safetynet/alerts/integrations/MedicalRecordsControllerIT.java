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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordsControllerIT {
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
    public void testAddMedicalRecord() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "Prenom");
        input.put("lastName","Nom");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(post("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("firstName", is("Prenom")))
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    public void testAddMedicalRecordInvalidInput() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", null);
        input.put("lastName","Nom");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(post("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testAddMedicalRecordAlreadyExist() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(post("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyMedicalRecord() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(put("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("firstName", is("John")))
                .andExpect(jsonPath("birthdate", is("01/12/2001")));
    }

    @Test
    public void testModifyMedicalRecordInvalidInput() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", null);
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(put("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyMedicalRecordNotFound() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "Hello");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(put("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(delete("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("firstName", is("John")));
    }

    @Test
    public void testDeleteMedicalRecordNotFound() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "Hello");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(delete("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMedicalRecordInvalidInput() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", null);
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        mockMvc.perform(delete("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
