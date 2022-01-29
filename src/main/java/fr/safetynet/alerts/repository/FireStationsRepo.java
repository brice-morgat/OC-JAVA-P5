package fr.safetynet.alerts.repository;

import fr.safetynet.alerts.models.FireStation;

import java.util.ArrayList;
import java.util.List;

public class FireStationsRepo {
    public static List<FireStation> fireStations = new ArrayList<>();

    public static void addFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
    }

    public static void removeFireStation(String address, int station) {
        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress() == address && fireStation.getStation() == station) {
                fireStations.remove(fireStation);
                return;
            }
        }
    }

    public static void modifyFireStations(FireStation fireStation) {
        int i = 0;
        for (FireStation fireStationEntity : fireStations) {
            if (fireStationEntity.getAddress() == fireStation.getAddress()) {
                fireStations.set(i, fireStationEntity);
                return;
            }
            i++;
        }
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
