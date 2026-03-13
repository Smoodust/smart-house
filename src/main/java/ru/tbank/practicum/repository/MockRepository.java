package ru.tbank.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.dot.Heater;

import java.util.HashMap;

@Repository
public class MockRepository implements HeaterRepository {
    HashMap<Long, Heater> heaters = new HashMap<>();

    @Override
    public Heater getHeaterbyId(Long heaterId) {
        if (heaters.containsKey(heaterId)) {
            return heaters.get(heaterId);
        }
        return null;
    }

    @Override
    public void createNewHeater(Long heaterId, Long temperature) {
        if (heaters.containsKey(heaterId)) {
            return;
        }
        heaters.put(heaterId, new Heater(heaterId, temperature));
    }

    @Override
    public void changeTemperature(Long heaterId, Long temperature) {
        if (!heaters.containsKey(heaterId)) {
            return;
        }
        Heater temp = heaters.get(heaterId);
        temp.setTemperature(temperature);
        heaters.put(heaterId, temp);
    }
}
