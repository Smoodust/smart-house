package ru.tbank.practicum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.WeatherClient;
import ru.tbank.practicum.service.WeatherService;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {
    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void shouldUpdateWeatherLocation() {
        WeatherLocation location = new WeatherLocation();
        location.setLatitude(10);
        location.setLongtitude(20);

        WeatherAPIResponse.Main main = new WeatherAPIResponse.Main(30.0, 29.0, 25.0, 35.0, 1010, 50);
        WeatherAPIResponse response = new WeatherAPIResponse(null, List.of(), main, null, null, "TestCity");

        when(weatherClient.getWeather(10, 20)).thenReturn(response);
        when(weatherRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        WeatherLocation result = weatherService.updateWeather(location);

        assertNotNull(result);
        assertEquals(30.0, result.getTemperature());
    }

    @Test
    void shouldReturnNullWhenTemperatureMissing() {
        WeatherLocation location = new WeatherLocation();
        location.setLatitude(10);
        location.setLongtitude(20);

        when(weatherClient.getWeather(10, 20)).thenReturn(null);
        WeatherLocation result = weatherService.updateWeather(location);

        assertNull(result);
    }

    @Test
    void shouldReturnExistingLocation() {
        WeatherLocation location = new WeatherLocation();
        location.setLatitude(10.0);
        location.setLongtitude(20.0);

        when(weatherRepository.findByLatitudeAndLongtitude(10.0, 20.0)).thenReturn(Optional.of(location));
        WeatherLocation result = weatherService.getNearestWeatherLocation(10.02, 20.04);

        assertEquals(location, result);
    }

    @Test
    void shouldCreateNewLocationIfNotFound() {
        when(weatherRepository.findByLatitudeAndLongtitude(anyDouble(), anyDouble()))
                .thenReturn(Optional.empty());

        WeatherAPIResponse.Main main = new WeatherAPIResponse.Main(15.0, 29.0, 25.0, 35.0, 1010, 50);

        WeatherAPIResponse response = new WeatherAPIResponse(null, List.of(), main, null, null, "TestCity");

        when(weatherClient.getWeather(anyDouble(), anyDouble())).thenReturn(response);
        when(weatherRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        WeatherLocation result = weatherService.getNearestWeatherLocation(10.12, 20.19);

        assertNotNull(result);
        assertEquals(15.0, result.getTemperature());
    }
}
