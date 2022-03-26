package fr.safetynet.alerts.unitaires.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.controllers.FireStationController;
import fr.safetynet.alerts.controllers.MedicalRecordController;
import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.FireStation;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.FireStationsService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FireStationsService fireStationsService;

    @Test
    public void testAddFireStation() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        mockMvc.perform(post("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void testAddFireStationInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        Mockito.when(fireStationsService.addFireStation(Mockito.any(FireStation.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(post("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFireStationAlreadyExist() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        Mockito.when(fireStationsService.addFireStation(Mockito.any(FireStation.class))).thenThrow(AlreadyExistException.class);
        mockMvc.perform(post("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyFireStation() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        mockMvc.perform(put("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testModifyFireStationInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        Mockito.when(fireStationsService.modifyFireStation(Mockito.any(FireStation.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(put("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyFireStationNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        Mockito.when(fireStationsService.modifyFireStation(Mockito.any(FireStation.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(put("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        mockMvc.perform(delete("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testDeleteFireStationInvalidInput() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        Mockito.when(fireStationsService.deleteFireStation(Mockito.any(JSONObject.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(delete("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteFireStationNotFound() throws Exception {
        JSONObject input = new JSONObject();
        input.put("station", 10);
        input.put("address", "Avenue de la république");
        Mockito.when(fireStationsService.deleteFireStation(Mockito.any(JSONObject.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(delete("/firestation").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetFire() throws Exception {
        mockMvc.perform(get("/fire").param("address", "Une adresse").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testGetFireNotFound() throws Exception {
        Mockito.when(fireStationsService.getPersonByAddress(Mockito.any(String.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/fire").param("address", "Une adresse").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetPhoneAlert() throws Exception {
        mockMvc.perform(get("/phoneAlert").param("firestation", "4").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testGetPhoneAlertNotFound() throws Exception {
        Mockito.when(fireStationsService.getPhoneAlert(Mockito.any(Integer.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/phoneAlert").param("firestation", "4").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetFloodStations() throws Exception {
        mockMvc.perform(get("/flood/stations").param("stations", "1,2,3").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testGetFloodStationsNotFound() throws Exception {
        Mockito.when(fireStationsService.getFloodStation(Mockito.any(Integer[].class))).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/flood/stations").param("stations", "1,2,3").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetFloodStationsInvalidInput() throws Exception {
        Mockito.when(fireStationsService.getFloodStation(Mockito.any(Integer[].class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(get("/flood/stations").param("stations", "1,2,3").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFireStation() throws Exception {
        mockMvc.perform(get("/firestation").param("stationNumber", "1").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testGetFireStationNotFound() throws Exception {
        Mockito.when(fireStationsService.getPersonByStation(Mockito.any(Integer.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/firestation").param("stationNumber", "1").contentType(MediaType.APPLICATION_JSON)
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
