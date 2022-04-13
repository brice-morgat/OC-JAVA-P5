package fr.safetynet.alerts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Person {
    private String firstName;

    private String lastName;

    private String email;

    private String city;

    private Integer zip;

    private String address;

    private String phone;
}