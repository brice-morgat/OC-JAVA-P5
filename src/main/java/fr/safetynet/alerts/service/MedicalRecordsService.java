package fr.safetynet.alerts.service;

import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordsService {
    public JSONObject addMedicalRecord(MedicalRecord medicalRecord) {
        JSONObject medicalRecordResult = new JSONObject();
        if (medicalRecord.firstName != null && medicalRecord.lastName != null && medicalRecord.birthdate != null && medicalRecord.medications != null && medicalRecord.allergies != null) {
            MedicalRecord result = MedicalRecordsRepo.addMedicalRecord(medicalRecord);
            medicalRecordResult.put("firstName", result.firstName);
            medicalRecordResult.put("lastName", result.lastName);
            medicalRecordResult.put("birthdate", result.birthdate);
            medicalRecordResult.put("medication", result.medications);
            medicalRecordResult.put("allergies", result.allergies);
        }
        return medicalRecordResult;
    }

    public JSONObject modifyMedicalRecord(MedicalRecord medicalRecord) {
        JSONObject medicalRecordResult = new JSONObject();
        MedicalRecord result = MedicalRecordsRepo.modifyMedicalRecord(medicalRecord);
        if (result != null) {
            medicalRecordResult.put("firstName", result.firstName);
            medicalRecordResult.put("lastName", result.lastName);
            medicalRecordResult.put("birthdate", result.birthdate);
            medicalRecordResult.put("medication", result.medications);
            medicalRecordResult.put("allergies", result.allergies);
        }
        return medicalRecordResult;
    }

    public JSONObject deleteMedicalRecord(MedicalRecord medicalRecord) {
        JSONObject medicalRecordResult = new JSONObject();
        if (medicalRecord.firstName != null && medicalRecord.lastName != null) {
            MedicalRecord result = MedicalRecordsRepo.deleteMedicalRecord(medicalRecord.firstName, medicalRecord.lastName);
            medicalRecordResult.put("firstName", result.firstName);
            medicalRecordResult.put("lastName", result.lastName);
            medicalRecordResult.put("birthdate", result.birthdate);
            medicalRecordResult.put("medication", result.medications);
            medicalRecordResult.put("allergies", result.allergies);
        }
        return medicalRecordResult;
    }
}
