package ru.tbank.practicum.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.tbank.practicum.repository.settings.Setting;

@Getter
@Setter
@AllArgsConstructor
public class DeviceDTO {
    private Long id;
    private Long idModel;
    private String name;
    private Setting setting;

    public DeviceDTO(DeviceTemp deviceTemp) {
        this.id = deviceTemp.getId();
        this.idModel = deviceTemp.getModel().getModelId();
        this.name = deviceTemp.getName();
        this.setting = deviceTemp.getSetting();
    }
}
