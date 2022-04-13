package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.models.Person;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsRepo {
    private static MedicalRecordsRepo instance;
    public static List<MedicalRecord> medicalRecords;

    private MedicalRecordsRepo() {
        medicalRecords = new ArrayList<>();
    }

    public static MedicalRecordsRepo getInstance() {
        if (instance == null) {
            instance = new MedicalRecordsRepo();
        }
        return instance;
    }

    public void clearMedicalRecords() {
        medicalRecords.clear();
    }

    /**
     * Add medicalRecord
     * @param medicalRecord
     * @return
     */
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    /**
     * Delete Medical Record
     * @param firstName
     * @param lastName
     * @return
     */
    public MedicalRecord deleteMedicalRecord(String firstName, String lastName) {
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

    /**
     * Modify MedicalRecord
     * @param medicalRecord
     * @return
     */
    public MedicalRecord modifyMedicalRecord(MedicalRecord medicalRecord) {
        int i = 0;
        for (MedicalRecord medicalRecordEntity : medicalRecords) {
            if (medicalRecordEntity.getFirstName().equals(medicalRecord.getFirstName()) && medicalRecordEntity.getLastName().equals(medicalRecord.getLastName())) {
                medicalRecords.set(i, medicalRecord);
                return medicalRecord;
            }
            i++;
        }
        return null;
    }

    /**
     * Get MedicalRecord By Name and Firstname
     * @param firstName
     * @param lastName
     * @return
     */
    public MedicalRecord getMedicalRecordByNameAndFirstName(String firstName, String lastName) {
        MedicalRecord medicalRecordToSearch = new MedicalRecord();
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return medicalRecord;
            }
        }
        return medicalRecordToSearch;
    }
}
