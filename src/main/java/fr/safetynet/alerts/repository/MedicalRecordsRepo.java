package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsRepo {
    public static List<MedicalRecord> medicalRecords = new ArrayList<>();

    public static void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }

    public static void removeMedicalRecord(String firstName, String lastName) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName() == firstName && medicalRecord.getLastName() == lastName) {
                medicalRecords.remove(medicalRecord);
                return;
            }
        }
    }

    public static void modifyMedicalRecord(MedicalRecord medicalRecord) {
        int i = 0;
        for (MedicalRecord medicalRecordEntity : medicalRecords) {
            if (medicalRecordEntity.getFirstName() == medicalRecordEntity.getFirstName() && medicalRecordEntity.getLastName() == medicalRecordEntity.getLastName()) {
                medicalRecords.set(i, medicalRecord);
                return;
            }
            i++;
        }
    }

    public static MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        MedicalRecord medicalRecordToSearch = new MedicalRecord();
        medicalRecordToSearch.setFirstName(firstName);
        medicalRecordToSearch.setLastName(lastName);
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return medicalRecord;
            }
        }
        return medicalRecordToSearch;
    }
}
