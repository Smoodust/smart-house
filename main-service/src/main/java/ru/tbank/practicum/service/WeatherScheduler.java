package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;

@RequiredArgsConstructor
@Service
public class WeatherScheduler {
  private final WeatherService weatherService;
  private final WeatherRepository weatherRepository;

  @Scheduled(cron = "0 0 * * * *")
  public void updateWeatherLocationInfo() {
    Pageable page = PageRequest.of(0, 100);
    Page<WeatherLocation> result = weatherRepository.findAll(page);
    while (result.hasContent()) {
      result.forEach(wl -> weatherService.updateWeather(wl));
      page = page.next();
      result = weatherRepository.findAll(page);
    }
  }
}
