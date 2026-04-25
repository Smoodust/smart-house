package ru.tbank.practicum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tbank.practicum.repository.WeatherRepository;
import ru.tbank.practicum.repository.entity.WeatherLocation;
import ru.tbank.practicum.service.WeatherService;
import ru.tbank.practicum.service.dto.WeatherAPIResponse;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {
    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WebClient webClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private WeatherService weatherService;

    private final String apiKey = "test-key";

    @Test
    void shouldReturnWeatherResponse() {
        WeatherAPIResponse.Main main = new WeatherAPIResponse.Main(25.0, 0, 0, 0, 0, 0);
        WeatherAPIResponse response = new WeatherAPIResponse(null, null, main, null, null, null);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(WeatherAPIResponse.class)).thenReturn(Mono.just(response));

        WeatherAPIResponse result = weatherService.getWeather(10, 20);

        assertNotNull(result);
        assertEquals(25.0, result.main().temp());
    }

    @Test
    void shouldUpdateWeatherLocation() {
        WeatherLocation location = new WeatherLocation();
        location.setLatitude(10);
        location.setLongtitude(20);

        WeatherAPIResponse.Main main = new WeatherAPIResponse.Main(30.0, 29.0, 25.0, 35.0, 1010, 50);

        WeatherAPIResponse response = new WeatherAPIResponse(null, List.of(), main, null, null, "TestCity");

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(WeatherAPIResponse.class)).thenReturn(Mono.just(response));

        when(weatherRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        WeatherLocation result = weatherService.updateWeather(location);

        assertNotNull(result);
        assertEquals(30.0, result.getTemperature());
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

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(WeatherAPIResponse.class)).thenReturn(Mono.just(response));

        when(weatherRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        WeatherLocation result = weatherService.getNearestWeatherLocation(10.12, 20.19);

        assertNotNull(result);
        assertEquals(15.0, result.getTemperature());
    }
}
