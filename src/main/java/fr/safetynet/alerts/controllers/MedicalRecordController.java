package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;
import fr.safetynet.alerts.service.MedicalRecordsService;
import fr.safetynet.alerts.service.PersonsService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public class MedicalRecordController {
    private final MedicalRecordsService medicalRecordsService;

    public MedicalRecordController(MedicalRecordsService medicalRecordsService) {
        this.medicalRecordsService = medicalRecordsService;
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public JSONObject addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordsService.addMedicalRecord(medicalRecord);
    }

    @PutMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public JSONObject modifyMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordsService.modifyMedicalRecord(medicalRecord);
    }

    @DeleteMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordsService.deleteMedicalRecord(medicalRecord);
        System.out.println("Personne supprimé");
    }
}
