package ru.tbank.practicum.repository.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device")
public class Device {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "device_id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "external_id", nullable = false)
  private String externalId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "model_id", nullable = false)
  private DeviceModel model;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HistoricalDeviceData> historicalDataList = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "last_historical_data_id")
  private HistoricalDeviceData lastHistoricalData;

  public void addNewData(HistoricalDeviceData data) {
    data.setDevice(this);
    historicalDataList.add(data);
    lastHistoricalData = data;
  }
}
