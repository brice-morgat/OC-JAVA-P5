package fr.safetynet.alerts.controllers;

import fr.safetynet.alerts.exceptions.AlreadyExistException;
import fr.safetynet.alerts.exceptions.InvalidInputException;
import fr.safetynet.alerts.exceptions.NotFoundException;
import fr.safetynet.alerts.models.MedicalRecord;
import fr.safetynet.alerts.service.MedicalRecordsService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * MedicalRecord Controller
 */
@RestController
public class MedicalRecordController {
    private final MedicalRecordsService medicalRecordsService;
    private static Logger log = Logger.getLogger(MedicalRecordController.class);


    public MedicalRecordController(MedicalRecordsService medicalRecordsService) {
        this.medicalRecordsService = medicalRecordsService;
    }

    /**
     * Add medical record
     * @param medicalRecord
     * @return
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity addMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws ParseException {
        log.debug("Ajouter un dossier médical");
        JSONObject response;
        try {
            response =  medicalRecordsService.addMedicalRecord(medicalRecord);
            log.info("Le dossier médical a bien été ajouter");
        } catch (InvalidInputException | AlreadyExistException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**
     * Modify Medical Record
     * @param medicalRecord
     * @return
     */
    @PutMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity modifyMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws ParseException {
        log.debug("Modifier un dossier médicale");
        JSONObject response;
        try {
            response =  medicalRecordsService.modifyMedicalRecord(medicalRecord);
            log.info("Le dossier a bien été modifier");
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Delete Medical Record
     * @param medicalRecord
     * @return
     */
    @DeleteMapping("/medicalRecord")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws ParseException {
        log.debug("Supprimer un dossier médical");
        JSONObject response;
        try {
            response = medicalRecordsService.deleteMedicalRecord(medicalRecord);
            log.info("Le dossier a bien été supprimer");
        } catch (InvalidInputException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
