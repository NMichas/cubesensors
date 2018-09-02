package com.github.nmichas.cubesensors.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataAlignedDTO {
  private long battery;
  private boolean charging;
  private String cube;
  private long firmware;
  private double humidity;
  private double light;
  private long noisedba;
  private long pressure;
  private long rssi;
  private double temp;
  private double voc;
  private long voc_resistance;
}
