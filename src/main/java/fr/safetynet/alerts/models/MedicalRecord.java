package fr.safetynet.alerts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MedicalRecord {
    public String firstName;
    public String lastName;
    public String birthdate;

    public List<String> medications;
    public List<String> allergies;

}
