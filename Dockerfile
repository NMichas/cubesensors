FROM azul/zulu-openjdk:8u181

COPY target/cubesensors-*.jar /opt/cubesensors/cubesensors.jar

ENTRYPOINT java -jar /opt/cubesensors/cubesensors.jar