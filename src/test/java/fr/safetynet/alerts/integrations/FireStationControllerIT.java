package fr.safetynet.alerts.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.repository.PersonsRepo;
import fr.safetynet.alerts.service.FireStationsService;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
import fr.safetynet.alerts.tools.JsonTools;
import org.json.simple.JSONObject;
import org.junit.Before;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationControllerIT {

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
    public void testAddFireStation() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        mockMvc.perform(post("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("station", is(10)))
                .andExpect(jsonPath("address", is("Avenue de la république")));
    }

    @Test
    public void testAddFireStationInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", null);
        input.put("address", "Avenue de la république");
        mockMvc.perform(post("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFireStationAlreadyExist() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        mockMvc.perform(post("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyFireStation() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 8);
        input.put("address", "834 Binoc Ave");
        mockMvc.perform(put("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("station", is(8)))
                .andExpect(jsonPath("address", is("834 Binoc Ave")));
    }

    @Test
    public void testModifyFireStationInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", null);
        input.put("address", "Avenue de la république");
        mockMvc.perform(put("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyFireStationNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        mockMvc.perform(put("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 3);
        input.put("address", "834 Binoc Ave");
        mockMvc.perform(delete("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("station", is(3)))
                .andExpect(jsonPath("address", is("834 Binoc Ave")));
    }

    @Test
    public void testDeleteFireStationInvalidInput() throws Exception {
        mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteFireStationNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", null);
        input.put("address", "Avenue de la république");
        mockMvc.perform(delete("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetFire() throws Exception {
        mockMvc.perform(get("/fire").param("address", "1509 Culver St").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andExpect(jsonPath("station", is(3)));
    }

    @Test
    public void testGetFireNotFound() throws Exception {
        mockMvc.perform(get("/fire").param("address", "Une adresse").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetPhoneAlert() throws Exception {
        mockMvc.perform(get("/phoneAlert").param("firestation", "3").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)));
    }

    @Test
    public void testGetPhoneAlertNotFound() throws Exception {
        mockMvc.perform(get("/phoneAlert").param("firestation", "89").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetFloodStations() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", "1,2,3").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(10)));
    }

    @Test
    public void testGetFloodStationsNotFound() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", "187").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetFloodStationsInvalidInput() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", "abc").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFireStation() throws Exception {
        mockMvc.perform(get("/firestation").param("stationNumber", "3").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.persons", hasSize(11)))
                .andExpect(jsonPath("adults", is(8)))
                .andExpect(jsonPath("childs", is(3)));
    }

    @Test
    public void testGetFireStationNotFound() throws Exception {
        mockMvc.perform(get("/firestation").param("stationNumber", "187").contentType(MediaType.APPLICATION_JSON)
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
