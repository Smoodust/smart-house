package ru.tbank.practicum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.HistoricalDataRepository;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.entity.DeviceModel;
import ru.tbank.practicum.repository.entity.HistoricalDeviceData;
import ru.tbank.practicum.repository.entity.Location;
import ru.tbank.practicum.repository.entity.User;
import ru.tbank.practicum.repository.settings.SettingDefinition;
import ru.tbank.practicum.service.DeviceService;
import ru.tbank.practicum.service.UserService;

class DeviceServiceTest {

  @Mock private DeviceRepository deviceRepository;

  @Mock private UserService userService;

  @Mock private HistoricalDataRepository historicalDataRepository;

  @Mock private UserDetails userDetails;

  @InjectMocks private DeviceService deviceService;

  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
  }

  @Test
  void getAllDevices_userExists_returnsDevices() {
    List<Device> devices = List.of(new Device(), new Device());

    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.of(user));
    when(deviceRepository.findAllByUserId(1L)).thenReturn(devices);

    List<Device> result = deviceService.getAllDevices(userDetails);

    assertEquals(2, result.size());
    verify(deviceRepository).findAllByUserId(1L);
  }

  @Test
  void getAllDevices_userNotFound_returnsEmptyList() {
    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.empty());

    List<Device> result = deviceService.getAllDevices(userDetails);

    assertTrue(result.isEmpty());
    verifyNoInteractions(deviceRepository);
  }

  @Test
  void getDeviceById_success() {
    Device device = mock(Device.class);
    Location location = mock(Location.class);

    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.of(user));
    when(deviceRepository.findById(10L)).thenReturn(Optional.of(device));

    when(device.getLocation()).thenReturn(location);
    when(location.getUser()).thenReturn(user);

    Device result = deviceService.getDeviceById(10L, userDetails);

    assertEquals(device, result);
  }

  @Test
  void getDeviceById_userNotFound_throwsException() {
    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.empty());

    assertThrows(
        IllegalArgumentException.class, () -> deviceService.getDeviceById(1L, userDetails));
  }

  @Test
  void getDeviceById_otherUserDevice_throwsException() {
    User anotherUser = new User();
    anotherUser.setId(2L);

    Device device = mock(Device.class);
    Location location = mock(Location.class);

    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.of(user));
    when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

    when(device.getLocation()).thenReturn(location);
    when(location.getUser()).thenReturn(anotherUser);

    assertThrows(
        IllegalArgumentException.class, () -> deviceService.getDeviceById(1L, userDetails));
  }

  @Test
  void updateDeviceState_validUpdate() {
    Device device = mock(Device.class);
    HistoricalDeviceData lastData = mock(HistoricalDeviceData.class);
    DeviceModel model = mock(DeviceModel.class);
    SettingDefinition settingDefinition = mock(SettingDefinition.class);

    Map<String, Object> oldSettings = new HashMap<>();
    oldSettings.put("volume", 10);

    Map<String, Object> newValues = Map.of("volume", 20);

    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.of(user));
    when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

    Location location = mock(Location.class);
    when(device.getLocation()).thenReturn(location);
    when(location.getUser()).thenReturn(user);

    when(device.getLastHistoricalData()).thenReturn(lastData);
    when(lastData.getSettings()).thenReturn(oldSettings);

    when(device.getModel()).thenReturn(model);
    when(model.getSetting("volume")).thenReturn(settingDefinition);
    when(settingDefinition.convertAndValidate(20)).thenReturn(20);

    deviceService.updateDeviceState(1L, newValues, userDetails);

    verify(device).addNewData(any(HistoricalDeviceData.class));
    verify(deviceRepository).save(device);
  }

  @Test
  void updateDeviceState_unknownSetting_ignored() {
    Device device = mock(Device.class);
    HistoricalDeviceData lastData = mock(HistoricalDeviceData.class);
    DeviceModel model = mock(DeviceModel.class);

    Map<String, Object> oldSettings = new HashMap<>();
    Map<String, Object> newValues = Map.of("unknown", 123);

    when(userService.getUserByUserDetail(userDetails)).thenReturn(Optional.of(user));
    when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

    Location location = mock(Location.class);
    when(device.getLocation()).thenReturn(location);
    when(location.getUser()).thenReturn(user);

    when(device.getLastHistoricalData()).thenReturn(lastData);
    when(lastData.getSettings()).thenReturn(oldSettings);

    when(device.getModel()).thenReturn(model);
    when(model.getSetting("unknown")).thenReturn(null);

    deviceService.updateDeviceState(1L, newValues, userDetails);

    verify(device).addNewData(any());
    verify(deviceRepository).save(device);
  }
}
