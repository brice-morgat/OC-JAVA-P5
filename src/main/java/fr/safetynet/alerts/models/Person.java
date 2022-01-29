package fr.safetynet.alerts.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Person {
    public String firstName;

    public String lastName;

    public String email;

    public String city;

    public Integer zip;

    public String address;

    public String phone;

}
