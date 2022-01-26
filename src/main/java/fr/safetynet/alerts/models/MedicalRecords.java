package fr.safetynet.alerts.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "medicalrecords")
public class MedicalRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String birthdate;

    private String medications;
    private String allergies;
}
