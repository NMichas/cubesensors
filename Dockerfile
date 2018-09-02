FROM azul/zulu-openjdk:8u181

COPY target/cubesensors-1.0.0-SNAPSHOT.jar /opt/cubesensors/

ENTRYPOINT java -jar /opt/cubesensors/cubesensors-1.0.0-SNAPSHOT.jar