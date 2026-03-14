package ru.tbank.practicum.repository.dot;

import ru.tbank.practicum.repository.settings.Setting;

public class DeviceDTO {
    private Long id;
    private Long idModel;
    private String name;
    private Setting setting;

    public Long getIdModel() {
        return idModel;
    }

    public void setIdModel(Long idModel) {
        this.idModel = idModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.idModel = device.getModel().getModelId();
        this.name = device.getName();
        this.setting = device.getSetting();
    }
}
