package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.FireStation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireStationsRepo {
    public static List<FireStation> fireStations = new ArrayList<>();

    public static FireStation addFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
        return fireStation;
    }

    public static FireStation removeFireStation(FireStation fireStation) {
        int i = 0;
        if (fireStation.station != null && fireStation.address != null) {
            for (FireStation fireStationEntity : fireStations) {
                if (fireStationEntity.getAddress().equals(fireStation.address) && fireStationEntity.getStation().equals(fireStation.station)) {
                    fireStations.remove(i);
                    return fireStation;
                }
            }
        } else {
            for (Iterator<FireStation> it = fireStations.iterator(); it.hasNext();) {
                FireStation fireStationEntity = it.next();
                if (fireStationEntity.getStation().equals(fireStation.station) && fireStation.address == null) {
                    it.remove();
                }
                if (fireStationEntity.getAddress().equals(fireStation.address) && fireStation.station == null) {
                    it.remove();
                }
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
}
