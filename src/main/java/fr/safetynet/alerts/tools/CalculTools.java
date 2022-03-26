package fr.safetynet.alerts.tools;

import fr.safetynet.alerts.service.PersonsService;
import org.apache.log4j.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class CalculTools {
    private static Logger log = Logger.getLogger(CalculTools.class);
    public static int ageParser(String birthdate)  {
        LocalDate localDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            localDate = LocalDate.parse(birthdate, formatter);

        } catch (DateTimeException e) {
//            int date = Integer.parseInt(birthdate.split("/")[2]);
//            LocalDate now = LocalDate.now();
//            return now.getYear() - date;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            localDate = LocalDate.parse(birthdate, formatter);
        } finally {
            LocalDate now = LocalDate.now();
            Period period = Period.between(localDate, now);
            return period.getYears();
        }
    }
}
