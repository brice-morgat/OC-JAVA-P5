package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.FireStation;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireStationsRepo {
    public static List<FireStation> fireStations = new ArrayList<>();

    public static FireStation addFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
        return fireStation;
    }

    public static List<FireStation> removeFireStation(FireStation fireStation) {
        List<FireStation> listDeletedFireStation = new ArrayList();
        int i = 0;
        if (fireStation.station != null && fireStation.address != null) {
            for (Iterator<FireStation> it = fireStations.iterator(); it.hasNext();) {
                FireStation fireStationEntity = it.next();
                if (fireStationEntity.getAddress().equals(fireStation.address) && fireStationEntity.getStation().equals(fireStation.station)) {
                    listDeletedFireStation.add(fireStationEntity);
                    it.remove();
                }
            }
            return listDeletedFireStation;
        } else {
            for (Iterator<FireStation> it = fireStations.iterator(); it.hasNext();) {
                FireStation fireStationEntity = it.next();
                if (fireStationEntity.getStation().equals(fireStation.station) && fireStation.address == null) {
                    listDeletedFireStation.add(fireStationEntity);
                    it.remove();
                }
                if (fireStationEntity.getAddress().equals(fireStation.address) && fireStation.station == null) {
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

    public static FireStation modifyFireStations(FireStation fireStation) {
        int i = 0;
        for (FireStation fireStationEntity : fireStations) {
            if (fireStationEntity.getAddress().equals(fireStation.address) && fireStation.station != null) {
                fireStations.set(i, fireStationEntity);
                return fireStation;
            }
            i++;
        }
        return null;
    }

    public static FireStation getFireStationByNumber(int station) {
        FireStation fireStationToSearch = new FireStation();
        fireStationToSearch.setStation(station);
        for (FireStation fireStation : fireStations) {
            if (fireStation.getStation().equals(station)) {
                return fireStation;
            }
        }
        return fireStationToSearch;
    }

    public static List getListAddressByStationNumber(int station_number) {
        List addresses = new ArrayList();
        for (FireStation fireStation: fireStations) {
            if (fireStation.getStation().equals(station_number)) {
                addresses.add(fireStation.address);
            }
        }
        return addresses;
    }
}
