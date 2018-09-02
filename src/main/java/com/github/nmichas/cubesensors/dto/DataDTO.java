package com.github.nmichas.cubesensors.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDTO {
  private long battery;
  private long charging;
  private String cube;
  private long firmware;
  private long humidity;
  private long light;
  private long noisedba;
  private long pressure;
  private long rssi;
  private long temp;
  private long voc;
  private long voc_resistance;
}
