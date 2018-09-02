package com.github.nmichas.cubesensors.services;

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
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataService {

  private final String cubesensorsUrl = "193.164.138.6";
  private final String cubesensorsHost = "data.cubesensors.com";
  private final Map<String, DataDTO> data = new HashMap<>();

  public DataService() {
  }

  public String updateOrigin(String baseStationId, SensorDTO sensorDTO) throws IOException {
    OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(sensorDTO));
    Request request = new Request.Builder()
        .url("http://" + cubesensorsUrl + "/v1/devices/" + baseStationId)
        .addHeader("host", cubesensorsHost)
        .build();
    Response response = client.newCall(request).execute();

    return response.body().string();
  }

  public void updateLocal(SensorDTO sensorDTO) {
    for (DataDTO dataDTO : sensorDTO.getData()) {
      data.put(dataDTO.getCube(), dataDTO);
    }
  }

  public DataDTO getLocal(String cube) {
    return data.get(cube);
  }

  /**
   * Sample output:
   * {
   *     "device": {
   *         "cubes": [
   *             "REDACTED",
   *             "REDACTED"
   *         ],
   *         "extra": {
   *             "cubes": [
   *                 {
   *                     "extra": {
   *                         "roomtype": "live"
   *                     },
   *                     "mark": "REDACTED",
   *                     "type": "cube",
   *                     "uid": "REDACTED"
   *                 },
   *                 {
   *                     "extra": {
   *                         "roomtype": "sleep"
   *                     },
   *                     "mark": "REDACTED",
   *                     "type": "cube",
   *                     "uid": "REDACTED"
   *                 }
   *             ],
   *             "last": {
   *                 "firmware": "78",
   *                 "lifeline": "REDACTED",
   *                 "stored_time": "2018-09-02T09:58:00",
   *                 "timestamp": "1535882272",
   *                 "version": "9b007bf"
   *             },
   *             "state": "working"
   *         },
   *         "mark": "REDACTED",
   *         "state": "working",
   *         "type": "base",
   *         "uid": "REDACTED"
   *     },
   *     "ok": true
   * }
   * @param baseStationId
   * @return
   * @throws URISyntaxException
   * @throws IOException
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
