BUILD_TAG := $(shell mvn -q -Dexec.executable=echo -Dexec.args='$${project.version}' --non-recursive exec:exec)

all: build push

build:
	echo Building image with tag: $(BUILD_TAG)
	mvn clean install
	docker build . -t nassos/cubesensors:$(BUILD_TAG) -t nassos/cubesensors

push:
	docker push nassos/cubesensors:$(BUILD_TAG)
	docker push nassos/cubesensors:latest