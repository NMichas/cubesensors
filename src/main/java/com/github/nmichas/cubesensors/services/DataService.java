package com.github.nmichas.cubesensors.services;

import com.github.nmichas.cubesensors.dto.DataAlignedDTO;
import com.github.nmichas.cubesensors.dto.DataDTO;
import com.github.nmichas.cubesensors.dto.SensorDTO;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DataService {
  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(DataService.class.getName());

  private final String cubesensorsUrl = "193.164.138.6";
  private final String cubesensorsHost = "data.cubesensors.com";
  private final Map<String, DataDTO> data = new HashMap<>();

  private void correctValues(SensorDTO sensorDTO) {

  }

  public String updateOrigin(String baseStationId, SensorDTO sensorDTO) throws IOException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url("http://" + cubesensorsUrl + "/v1/devices/" + baseStationId)
        .addHeader("host", cubesensorsHost)
        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(sensorDTO)))
        .build();
    Response response = client.newCall(request).execute();

    return response.body().string();
  }

  public void updateLocal(SensorDTO sensorDTO) {
    for (DataDTO dataDTO : sensorDTO.getData()) {
      data.put(dataDTO.getCube(), dataDTO);
    }
  }

  /**
   * https://www.visionect.com/blog/a-home-environment-sensor/
   *
   * Sample: DataDTO(battery=2607, charging=2, cube=REDACTED, firmware=78, humidity=4680, light=4064, noisedba=49,
   * pressure=960, rssi=-67, temp=2951, voc=0, voc_resistance=640034342)
   */
  public DataAlignedDTO getLocal(String cube) {
    LOGGER.log(Level.INFO, "Requested data for cube: {0}.", cube);

    DataDTO c = data.get(cube);
    if (c == null) {
      LOGGER.log(Level.WARNING, "Requested data for cube {0}, however this server never received any.", cube);
      return null;
    }
    LOGGER.log(Level.INFO, "{0}: Original: {1}", new Object[]{cube, c});

    DecimalFormat df = new DecimalFormat("#.##");
    df.setRoundingMode(RoundingMode.CEILING);

    DataAlignedDTO dataAlignedDTO = DataAlignedDTO.builder()
        .battery(c.getBattery())
        .charging(c.getCharging() == 3 ? true : false)
        .cube(c.getCube())
        .firmware(c.getFirmware())
        .humidity(Double.valueOf(df.format(c.getHumidity() / 100.0)))
        .light(Double.valueOf(df.format(10 / 6.0 * (1 + (c.getLight() / 1024.0) * 4.787 * Math
            .exp(-(Math.pow((c.getLight() - 2048) / 400.0 + 1, 2) / 50.0))) * (102400.0 / Math.max(15, c.getLight())
            - 25))))
        .noisedba(c.getNoisedba())
        .pressure(c.getPressure())
        .rssi(c.getRssi())
        .temp(Double.valueOf(df.format(c.getTemp() / 100.0)))
        .voc(Double.valueOf(df.format(Math.max(c.getVoc() - 900, 0) * 0.4 + Math.min(c.getVoc(), 900))))
        .voc_resistance(c.getVoc_resistance())
        .build();
    LOGGER.log(Level.INFO, "{0}: Aligned: {1}", new Object[]{cube, dataAlignedDTO});

    return dataAlignedDTO;
  }

  /**
   * Sample output: { "device": { "cubes": [ "REDACTED", "REDACTED" ], "extra": { "cubes": [ { "extra": { "roomtype":
   * "live" }, "mark": "REDACTED", "type": "cube", "uid": "REDACTED" }, { "extra": { "roomtype": "sleep" }, "mark":
   * "REDACTED", "type": "cube", "uid": "REDACTED" } ], "last": { "firmware": "78", "lifeline": "REDACTED",
   * "stored_time": "2018-09-02T09:58:00", "timestamp": "1535882272", "version": "9b007bf" }, "state": "working" },
   * "mark": "REDACTED", "state": "working", "type": "base", "uid": "REDACTED" }, "ok": true }
   */
  public String getDevices(String baseStationId) throws URISyntaxException, IOException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url("http://" + cubesensorsUrl + "/v1/devices/" + baseStationId)
        .addHeader("host", cubesensorsHost)
        .build();
    Response response = client.newCall(request).execute();

    return response.body().string();
  }

}
