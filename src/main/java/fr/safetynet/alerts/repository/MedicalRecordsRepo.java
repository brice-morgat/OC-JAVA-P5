package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsRepo {
    public static List<MedicalRecord> medicalRecords = new ArrayList<>();

    public static MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    public static MedicalRecord deleteMedicalRecord(String firstName, String lastName) {
        int i = 0;
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                medicalRecords.remove(i);
                return medicalRecord;
            }
            i++;
        }
        return null;
    }

    public static MedicalRecord modifyMedicalRecord(MedicalRecord medicalRecord) {
        int i = 0;
        for (MedicalRecord medicalRecordEntity : medicalRecords) {
            if (medicalRecordEntity.getFirstName() == medicalRecordEntity.getFirstName() && medicalRecordEntity.getLastName() == medicalRecordEntity.getLastName()) {
                medicalRecords.set(i, medicalRecord);
                return medicalRecord;
            }
            i++;
        }
        return null;
    }

    public static MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        MedicalRecord medicalRecordToSearch = new MedicalRecord();
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return medicalRecord;
            }
        }
        return medicalRecordToSearch;
    }
}
