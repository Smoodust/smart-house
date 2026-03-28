package ru.tbank.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;

@Service
public class WeatherScheduler {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateWeatherLocationInfo() {
        Pageable page = PageRequest.of(0, 100);
        Page<WeatherLocation> result = weatherRepository.findAll(page);
        while (result.hasContent()) {
            result.stream().map(wl -> weatherService.updateWeather(wl));
            page = page.next();
        }
    }
}
