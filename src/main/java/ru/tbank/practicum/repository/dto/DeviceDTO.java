package ru.tbank.practicum.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.tbank.practicum.repository.entity.Device;
import ru.tbank.practicum.repository.settings.Setting;

@Getter
@Setter
@AllArgsConstructor
public class DeviceDTO {
    private Long id;
    private Long idModel;
    private String name;
    private Setting setting;

    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.idModel = device.getModel().getModelId();
        this.name = device.getName();
        this.setting =  new Setting(device.getModel(), device.getSettings());
    }
}
