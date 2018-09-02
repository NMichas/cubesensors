package com.github.nmichas.cubesensors.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {
  private String firmware;
  private long time;
  private DataDTO[] data;
}
