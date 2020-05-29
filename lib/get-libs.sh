#!/bin/bash
[ ! -f snakeyaml-1.23.jar ] && wget https://repo1.maven.org/maven2/org/yaml/snakeyaml/1.23/snakeyaml-1.23.jar
[ ! -f jackson-core-2.11.0.jar ] && wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.11.0/jackson-core-2.11.0.jar
[ ! -f jackson-databind-2.11.0.jar ] && wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.11.0/jackson-databind-2.11.0.jar
[ ! -f jackson-dataformat-yaml-2.11.0.jar ] && wget https://repo1.maven.org/maven2/com/fasterxml/jackson/dataformat/jackson-dataformat-yaml/2.11.0/jackson-dataformat-yaml-2.11.0.jar
[ ! -f jackson-annotations-2.11.0.jar ] && wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.11.0/jackson-annotations-2.11.0.jar