package fr.safetynet.alerts.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicalRecords {
    public String firstName;
    public String lastName;
    public String birthdate;

    public String medications;
    public String allergies;
}
