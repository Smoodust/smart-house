package ru.tbank.practicum.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.tbank.practicum.repository.settings.SettingDefinition;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private DeviceModel model;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> setting = new HashMap<>();

    public void updateSettings(Map<String, Object> updates) {
        HashMap<String, Object> converted = new HashMap<>();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String name = entry.getKey();
            SettingDefinition current = model.getSetting(name);
            converted.put(name, current.convertAndValidate(entry.getValue()));
        }
        setting.putAll(converted);
    }
}
