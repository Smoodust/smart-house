package ru.tbank.practicum;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.service.DeviceService;
import ru.tbank.practicum.service.KafkaDataConsumer;
import ru.tbank.practicum.service.UserService;
import ru.tbank.practicum.service.dto.DeviceDataKafkaDTO;

@ExtendWith(MockitoExtension.class)
class KafkaDataConsumerTest {

  @Mock private UserService userService;
  @Mock private DeviceRepository deviceRepository;
  @Mock private DeviceService deviceService;

  @InjectMocks private KafkaDataConsumer consumer;

  private final ObjectMapper mapper = new ObjectMapper();

  private DeviceDataKafkaDTO validDto;
  private User user;
  private Device device;
  private HashMap<String, Object> data;

  @BeforeEach
  void setUp() {
    data = new HashMap<>();
    validDto = new DeviceDataKafkaDTO();
    validDto.setLogin("testUser");
    validDto.setPass("testPass");
    validDto.setDeviceId("ext-123");
    validDto.setData(data);

    user = new User();
    device = new Device();
  }

  @Test
  void shouldUpdateDeviceStateWhenMessageIsValidAndDeviceExists() throws Exception {
    String jsonMessage = mapper.writeValueAsString(validDto);
    when(userService.getUserByLoginAndPass("testUser", "testPass")).thenReturn(user);
    when(deviceRepository.findByExternalIdAndLocationUser("ext-123", user))
        .thenReturn(Optional.of(device));

    consumer.listen(jsonMessage);

    verify(deviceService).updateDeviceState(device, data);
  }

  @Test
  void shouldNotProceedWhenMessageCannotBeParsed() {
    String invalidJson = "not a json";

    consumer.listen(invalidJson);

    verify(userService, never()).getUserByLoginAndPass(any(), any());
    verify(deviceRepository, never()).findByExternalIdAndLocationUser(any(), any());
    verify(deviceService, never()).updateDeviceState(any(), any());
  }

  @Test
  void shouldNotUpdateDeviceWhenDeviceNotFound() throws Exception {
    String jsonMessage = mapper.writeValueAsString(validDto);
    when(userService.getUserByLoginAndPass("testUser", "testPass")).thenReturn(user);
    when(deviceRepository.findByExternalIdAndLocationUser("ext-123", user))
        .thenReturn(Optional.empty());

    consumer.listen(jsonMessage);

    verify(deviceService, never()).updateDeviceState(any(), any());
  }

  @Test
  void shouldThrowWhenUserIsNullAndRepositoryRequiresNonNull() throws Exception {
    String jsonMessage = mapper.writeValueAsString(validDto);
    when(userService.getUserByLoginAndPass("testUser", "testPass")).thenReturn(null);
    // Simulate repository rejecting null user
    when(deviceRepository.findByExternalIdAndLocationUser("ext-123", null))
        .thenThrow(new IllegalArgumentException("User must not be null"));

    assertThatThrownBy(() -> consumer.listen(jsonMessage))
        .isInstanceOf(IllegalArgumentException.class);

    verify(deviceService, never()).updateDeviceState(any(), any());
  }

  @Test
  void shouldSkipUpdateWhenUserIsNullAndRepositoryReturnsEmpty() throws Exception {
    String jsonMessage = mapper.writeValueAsString(validDto);
    when(userService.getUserByLoginAndPass("testUser", "testPass")).thenReturn(null);
    when(deviceRepository.findByExternalIdAndLocationUser("ext-123", null))
        .thenReturn(Optional.empty());

    consumer.listen(jsonMessage);

    verify(deviceService, never()).updateDeviceState(any(), any());
  }
}
