package fr.safetynet.alerts.unitaires.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.safetynet.alerts.controllers.MedicalRecordController;
import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.service.MedicalRecordsService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordsService medicalRecordsService;

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
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
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
        Mockito.when(medicalRecordsService.addMedicalRecord(Mockito.any(MedicalRecord.class))).thenThrow(InvalidInputException.class);
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
        Mockito.when(medicalRecordsService.addMedicalRecord(Mockito.any(MedicalRecord.class))).thenThrow(AlreadyExistException.class);
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
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testModifyMedicalRecordInvalidInput() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        Mockito.when(medicalRecordsService.modifyMedicalRecord(Mockito.any(MedicalRecord.class))).thenThrow(InvalidInputException.class);
        mockMvc.perform(put("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testModifyMedicalRecordNotFound() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        Mockito.when(medicalRecordsService.modifyMedicalRecord(Mockito.any(MedicalRecord.class))).thenThrow(NotFoundException.class);
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
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testDeleteMedicalRecordNotFound() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        Mockito.when(medicalRecordsService.deleteMedicalRecord(Mockito.any(MedicalRecord.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(delete("/medicalRecord").content(asJsonString(input)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMedicalRecordInvalidInput() throws Exception {
        ArrayList<String> list = new ArrayList();
        JSONObject input = new JSONObject();
        input.put("firstName", "John");
        input.put("lastName","Boyd");
        input.put("birthdate","01/12/2001");
        input.put("medications", list);
        input.put("allergies", list);
        Mockito.when(medicalRecordsService.deleteMedicalRecord(Mockito.any(MedicalRecord.class))).thenThrow(InvalidInputException.class);
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
