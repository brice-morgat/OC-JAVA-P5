package fr.safetynet.alerts.exceptions;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String t) {
        super(t);
    }
}