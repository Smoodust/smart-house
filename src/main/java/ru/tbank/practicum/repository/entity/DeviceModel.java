package ru.tbank.practicum.repository.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "device_model")
public class DeviceModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "model_id")
  private Long modelId;

  @Column(name = "model_name", nullable = false, unique = true)
  private String modelName;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<SettingDefinition> settings = new ArrayList<>();

  @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Device> devices;

  public SettingDefinition getSetting(String name) {
    return settings.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
  }
}
