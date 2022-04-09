package fr.safetynet.alerts.service;

import com.jsoniter.output.JsonStream;
import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.repository.MedicalRecordsRepo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordsService {
    /**
     * Add a medical record
     * @param medicalRecord
     * @return medical record added
     */
    public JSONObject addMedicalRecord(MedicalRecord medicalRecord) throws ParseException {
        if (medicalRecord.firstName != null && medicalRecord.lastName != null && medicalRecord.birthdate != null && medicalRecord.medications != null && medicalRecord.allergies != null) {
            if (!alreadyExist(medicalRecord)) {
                MedicalRecord result = MedicalRecordsRepo.addMedicalRecord(medicalRecord);
                JSONParser parser = new JSONParser();
                JSONObject medicalRecordResult = (JSONObject) parser.parse(JsonStream.serialize(result));
                return medicalRecordResult;
            } else {
                throw new AlreadyExistException("Le dossier médical existe déjà pour " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
            }
        } else {
            throw new InvalidInputException("Dossier médical non valide, il manque des informations");
        }
    }

    /**
     * Modify a medical record
     * @param medicalRecord
     * @return medical record's modified
     */
    public JSONObject modifyMedicalRecord(MedicalRecord medicalRecord) throws ParseException {
        if (medicalRecord.firstName != null && medicalRecord.lastName != null && medicalRecord.birthdate != null && medicalRecord.medications != null && medicalRecord.allergies != null) {
            MedicalRecord result = MedicalRecordsRepo.modifyMedicalRecord(medicalRecord);
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONObject medicalRecordResult = (JSONObject) parser.parse(JsonStream.serialize(result));
                return medicalRecordResult;
            } else {
                throw new NotFoundException("Dossier médical introuvable");
            }
        } else {
            throw new InvalidInputException("Format du dossier invalide");
        }
    }

    /**
     * Delete a medical record
     * @param medicalRecord
     * @return medical record deleted
     */
    public JSONObject deleteMedicalRecord(MedicalRecord medicalRecord) throws ParseException {
        if (medicalRecord.firstName != null && medicalRecord.lastName != null) {
            MedicalRecord result = MedicalRecordsRepo.deleteMedicalRecord(medicalRecord.firstName, medicalRecord.lastName);
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONObject medicalRecordResult = (JSONObject) parser.parse(JsonStream.serialize(result));
                return medicalRecordResult;
            } else  {
                throw new NotFoundException("Le dossier à supprimé n'existe pas ou est introuvable");
            }
        } else {
            throw new InvalidInputException("Nom ou prénom invalide");
        }
    }

    /**
     * Verify if a medical record already exist
     * @param medicalRecord
     * @return boolean
     */
    public boolean alreadyExist(MedicalRecord medicalRecord) {
        int i = 0;
        for (MedicalRecord entity : MedicalRecordsRepo.medicalRecords) {
            if (entity.getFirstName().equals(medicalRecord.firstName) && entity.getLastName().equals(medicalRecord.lastName)) {
                return true;
            }
            i++;
        }
        return false;
    }
}
