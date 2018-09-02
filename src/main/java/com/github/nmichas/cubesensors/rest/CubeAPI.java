package com.github.nmichas.cubesensors.rest;

import com.github.nmichas.cubesensors.dto.DataAlignedDTO;
import com.github.nmichas.cubesensors.dto.SensorDTO;
import com.github.nmichas.cubesensors.services.DataService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CubeAPI {
  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(CubeAPI.class.getName());

  private final DataService updateService;

  public CubeAPI(DataService originUpdateService) {
    this.updateService = originUpdateService;
  }

  @PostMapping(value = "/v1/devices/{baseStationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void devices(@PathVariable String baseStationId, @RequestBody SensorDTO sensorDTO) throws IOException {
    LOGGER.log(Level.INFO, "Intercepted: [POST] /v1/devices/{0}.", baseStationId);
    LOGGER.log(Level.INFO, sensorDTO.toString());
    String updateReply = updateService.updateOrigin(baseStationId, sensorDTO);
    LOGGER.log(Level.INFO, updateReply);

    updateService.updateLocal(sensorDTO);
  }

  @GetMapping(value = "/v1/devices/{baseStationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public String devices(@PathVariable String baseStationId) throws URISyntaxException, IOException {
    LOGGER.log(Level.INFO, "Intercepted: [GET] /v1/devices/{0}.", baseStationId);
    String getReply = updateService.getDevices(baseStationId);
    LOGGER.log(Level.INFO, getReply);

    return getReply;
  }

  @GetMapping(value = "/data/{cube}")
  private DataAlignedDTO data(@PathVariable String cube){
    return updateService.getLocal(cube);
  }
}
