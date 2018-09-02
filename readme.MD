# Cubesensors MITM

![Cubesensors](https://github.com/nmichas/cubesensors/raw/master/cs.jpg "Cubesensors")


MITM server for Cubesensors. It collects all data reported by your Cubes,
replays all requests back to Cubesensors' API servers and provides a
local endpoint to query for latest data.

## Prerequisites
* Unless you have found a way to SSH into Cubesensors' gateway (therefore
you could use something like IPTABLES; let me know in that case), you need
to have a router in which you can specify static DNS entries. In particular,
you need to be able to resolve `data.cubesensors.com` into the IP in which
you run this server. This will trick the Cubesensors gateway into contacting
this little project here instead of the real Cubesensors server.

## Docker support
The easiest way to quickly get up and running is to use Docker by
executing:

`docker run -d -p 80:80 --restart always --name cubesensors nassos/cubesensors`

## Data API (local)
To get the values of your cubes you can issue a `GET` request such as:

```
curl http://IP-WHERE-YOU-RUN-THIS/data/CUBEID
```
Make sure you replace `IP-WHERE-YOU-RUN-THIS` and `CUBEID`. If you do not
already know the IDs of your cubes, you can check on the log output of
this server once the first request from Cubes are properly captured (check the _cube=_ value), e.g.

```
2018-09-02 19:13:24.304  INFO 6 --- [p-nio-80-exec-7] c.g.nmichas.cubesensors.rest.CubeAPI
: SensorDTO(firmware=78, time=1535915604, data=[DataDTO(battery=2608,
charging=2, cube=REDACTED, firmware=78, humidity=4860, light=4088,
noisedba=48, pressure=963, rssi=-79, temp=2983, voc=0, voc_resistance=3604403926),
DataDTO(battery=2593, charging=2, cube=REDACTED, firmware=78,
humidity=5244, light=4075, noisedba=43, pressure=962, rssi=-77, temp=2627,
voc=551, voc_resistance=107008)])
```

## OpenHAB support
If you're using OpenHAB, here is a quick configuration to pull all the values
from your sensors into custom _Items_ using an HTTP binding. Be aware that
you need to know (and replace below) the IDs of your cubes.

* Define cached items for the HTTP binding for *each* of your cubes in `http.cfg`:
    ```
    cubesensorsFoo.url=http://SERVER-IP/data/Foo
    cubesensorsFoo.updateInterval=60000
    ```

* Define the Items:
    ```
    Number      Foo_CS_Humidity         {http="<[cubesensorsFoo:60000:JSONPATH($.humidity)]"}
    Number      Foo_CS_Light            {http="<[cubesensorsFoo:60000:JSONPATH($.light)]"}
    Number      Foo_CS_Noisedba         {http="<[cubesensorsFoo:60000:JSONPATH($.noisedba)]"}
    Number      Foo_CS_Pressure         {http="<[cubesensorsFoo:60000:JSONPATH($.pressure)]"}
    Number      Foo_CS_Temperature      {http="<[cubesensorsFoo:60000:JSONPATH($.temp)]"}
    Number      Foo_CS_Voc              {http="<[cubesensorsFoo:60000:JSONPATH($.voc)]"}

    ```