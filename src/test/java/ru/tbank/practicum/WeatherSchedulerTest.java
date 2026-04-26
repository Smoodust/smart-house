package ru.tbank.practicum;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.WeatherScheduler;
import ru.tbank.practicum.service.WeatherService;

@ExtendWith(MockitoExtension.class)
class WeatherSchedulerTest {

  @Mock private WeatherService weatherService;

  @Mock private WeatherRepository weatherRepository;

  @InjectMocks private WeatherScheduler weatherScheduler;

  @Test
  void shouldUpdateAllWeatherLocations() {
    WeatherLocation loc1 = new WeatherLocation();
    WeatherLocation loc2 = new WeatherLocation();

    Page<WeatherLocation> page = new PageImpl<>(List.of(loc1, loc2));

    when(weatherRepository.findAll(any(Pageable.class)))
        .thenReturn(page) // first call
        .thenReturn(Page.empty()); // second call → stops loop

    weatherScheduler.updateWeatherLocationInfo();

    verify(weatherService).updateWeather(loc1);
    verify(weatherService).updateWeather(loc2);
  }
}
