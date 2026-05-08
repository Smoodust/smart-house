package ru.tbank.practicum.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

@RequiredArgsConstructor
@Service
public class WeatherService {
  private final WeatherClient weatherClient;
  private final WeatherRepository weatherRepository;

  public WeatherLocation updateWeather(WeatherLocation weatherLocation) {
    Optional<WeatherAPIResponse> weatherResponse =
        Optional.ofNullable(
            weatherClient.getWeather(
                weatherLocation.getLatitude(), weatherLocation.getLongtitude()));
    Optional<Double> temperature =
        weatherResponse.map(WeatherAPIResponse::main).map(WeatherAPIResponse.Main::temp);
    if (temperature.isEmpty()) {
      return null;
    }
    weatherLocation.setTemperature(temperature.get());
    return weatherRepository.save(weatherLocation);
  }

  public WeatherLocation getNearestWeatherLocation(double lat, double lon) {
    lat = (double) Math.round(lat * 10) / 10;
    lon = (double) Math.round(lon * 10) / 10;

    Optional<WeatherLocation> loc = weatherRepository.findByLatitudeAndLongtitude(lat, lon);
    if (loc.isPresent()) {
      return loc.get();
    }

    WeatherLocation newLocation = new WeatherLocation();
    newLocation.setLatitude(lat);
    newLocation.setLongtitude(lon);
    return updateWeather(weatherRepository.save(newLocation));
  }
}
