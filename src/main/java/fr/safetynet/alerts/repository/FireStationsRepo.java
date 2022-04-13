package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.FireStation;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireStationsRepo {
    private static FireStationsRepo instance;
    private List<FireStation> fireStations;
    private static Logger log = Logger.getLogger(FireStationsRepo.class);

    private FireStationsRepo() {
        fireStations = new ArrayList<>();
    }

    public static FireStationsRepo getInstance() {
        if (instance == null) {
            instance = new FireStationsRepo();
        }
        return instance;
    }

    public void clearFireStations() {
        fireStations.clear();
    }

    public List<FireStation> getFireStations() {
        return fireStations;
    }

    /**
     * Add fireStation
     * @param fireStation
     * @return
     */
    public FireStation addFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
        return fireStation;
    }

    /**
     * Remove FireStation
     * @param fireStation
     * @return
     */
    public List<FireStation> removeFireStation(FireStation fireStation) {
        List<FireStation> listDeletedFireStation = new ArrayList();
        int i = 0;
        if (fireStation.getStation() != null && fireStation.getAddress() != null) {
            for (Iterator<FireStation> it = fireStations.iterator(); it.hasNext();) {
                FireStation fireStationEntity = it.next();
                if (fireStationEntity.getAddress().equals(fireStation.getAddress()) && fireStationEntity.getStation().equals(fireStation.getStation())) {
                    listDeletedFireStation.add(fireStationEntity);
                    it.remove();
                }
            }
            if(!listDeletedFireStation.isEmpty()){
                return listDeletedFireStation;
            }
        } else {
            for (Iterator<FireStation> it = fireStations.iterator(); it.hasNext();) {
                FireStation fireStationEntity = it.next();
                if (fireStationEntity.getStation().equals(fireStation.getStation()) && fireStation.getAddress() == null) {
                    listDeletedFireStation.add(fireStationEntity);
                    it.remove();
                }
                if (fireStationEntity.getAddress().equals(fireStation.getAddress()) && fireStation.getStation() == null) {
                    listDeletedFireStation.add(fireStationEntity);
                    it.remove();
                }
            }
            if (!listDeletedFireStation.isEmpty()) {
                return listDeletedFireStation;
            }
        }
        return null;
    }

    /**
     * Modify FireStation
     * @param fireStation
     * @return
     */
    public FireStation modifyFireStations(FireStation fireStation) {
        int i = 0;
        for (FireStation fireStationEntity : fireStations) {
            if (fireStationEntity.getAddress().equals(fireStation.getAddress()) && fireStation.getStation() != null) {
                fireStations.set(i, fireStationEntity);
                return fireStation;
            }
            i++;
        }
        return null;
    }

    /**
     * Get FireStation's number by address
     * @param address
     * @return
     */
    public int getFireStationNumberByAddress(String address) {
        for(FireStation fireStation: fireStations) {
            if (fireStation.getAddress().equals(address)) {
                return fireStation.getStation();
            }
        }
        return 0;
    }

    /**
     * Get List Address by Station's number
     * @param station_number
     * @return
     */
    public List getListAddressByStationNumber(int station_number) {
        List addresses = new ArrayList();
        for (FireStation fireStation: fireStations) {
            if (fireStation.getStation().equals(station_number)) {
                addresses.add(fireStation.getAddress());
            }
        }
        log.info(addresses);
        return addresses;
    }
}
