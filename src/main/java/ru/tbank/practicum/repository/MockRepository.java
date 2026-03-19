package ru.tbank.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.DeviceModel;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.repository.settings.BooleanDefinition;
import ru.tbank.practicum.repository.settings.NumberDefinition;
import ru.tbank.practicum.repository.settings.StringDefinition;

import java.util.*;

@Repository
public class MockRepository implements DeviceRepository, WeatherRepository {
    HashMap<Long, Device> devices = new HashMap<>();
    HashMap<Long, DeviceModel> deviceModels = new HashMap<>();
    List<WeatherLocation> weatherLocations = new ArrayList<>();

    public MockRepository() {
        BooleanDefinition powerSetting = new BooleanDefinition("turnOnOff", false);
        NumberDefinition temperatureSetting = new NumberDefinition("temperature", 0);
        StringDefinition colorHueSetting = new StringDefinition("color", "#000000");
        NumberDefinition positionOfBlindsSetting = new NumberDefinition("positionBlinds", 0);
        deviceModels.put(0L, new DeviceModel(0L, "Heater", new ArrayList<>(List.of(powerSetting, temperatureSetting))));
        deviceModels.put(1L, new DeviceModel(1L, "Simple Lamp", new ArrayList<>(List.of(powerSetting))));
        deviceModels.put(2L, new DeviceModel(2L, "Dance Lamp", new ArrayList<>(List.of(powerSetting, colorHueSetting))));
        deviceModels.put(3L, new DeviceModel(3L, "Blinds", new ArrayList<>(List.of(powerSetting, positionOfBlindsSetting))));

        devices.put(0L, new Device(0L, "Kitchen Heater", deviceModels.get(0L)));
        devices.put(1L, new Device(1L, "Bedroom Heater", deviceModels.get(0L)));
        devices.put(2L, new Device(2L, "Kitchen Lamp", deviceModels.get(1L)));
        devices.put(3L, new Device(3L, "Bedroom Lamp", deviceModels.get(1L)));
        devices.put(4L, new Device(4L, "Hallway Lamp", deviceModels.get(1L)));
        devices.put(5L, new Device(5L, "Club Lamp", deviceModels.get(2L)));
        devices.put(6L, new Device(6L, "Bedroom Blinds", deviceModels.get(3L)));
    }

    @Override
    public Device getDevicebyId(Long id) {
        return devices.get(id);
    }

    @Override
    public List<Device> getAllDevices() {
        return new ArrayList<>(devices.values());
    }

    @Override
    public void updateDeviceState(Long id, Map<String, Object> newValues) {
        Device currentDevice = devices.get(id);
        if (currentDevice == null) {
            return;
        }
        currentDevice.getSetting().setMap(newValues);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public WeatherLocation getNearestWeatherLocation(double lat, double lon) {
        for (WeatherLocation wl : weatherLocations) {
            if (calculateDistance(lat, lon, wl.getLatitude(), wl.getLongtitude()) < 10.0) {
                return wl;
            }
        }
        return null;
    }

    @Override
    public List<WeatherLocation> getAllWeatherLocations() {
        return weatherLocations;
    }

    @Override
    public void updateWeather(WeatherLocation newWeather) {
        for (WeatherLocation wl : weatherLocations) {
            if (calculateDistance(newWeather.getLatitude(), newWeather.getLongtitude(), wl.getLatitude(), wl.getLongtitude()) < 10.0) {
                wl.setTemperature(newWeather.getTemperature());
                return;
            }
        }
        weatherLocations.add(newWeather);
    }
}
