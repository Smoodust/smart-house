package ru.tbank.practicum.repository.entity;

import jakarta.persistence.*;

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

    @Column(name = "name", nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settings", columnDefinition = "json", nullable = false)
    private Map<String, Object> settings = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private DeviceModel model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public void updateSettings(Map<String, Object> updates) {
        HashMap<String, Object> converted = new HashMap<>();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String name = entry.getKey();
            SettingDefinition current = model.getSetting(name);
            converted.put(name, current.convertAndValidate(entry.getValue()));
        }
        settings.putAll(converted);
    }
}
