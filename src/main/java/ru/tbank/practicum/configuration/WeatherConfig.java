package ru.tbank.practicum.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WeatherConfig {

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Bean
    public RestClient weatherRestClient(RestClient.Builder builder) {
        return builder.baseUrl(weatherApiUrl).build();
    }
}
