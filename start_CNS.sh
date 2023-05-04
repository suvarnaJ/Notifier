#!/bin/sh

cd /TCLNotifier/repo/Notifier/Spring-Boot-Kafka-Producer-and-Consumer-Example-master/target/

export http_proxy=http://10.133.12.181:80

export https_proxy=https://10.133.12.181:80

nohup java -Djava.net.useSystemProxies=true  -jar  /TCLNotifier/repo/Notifier/Spring-Boot-Kafka-Producer-and-Consumer-Example-master/target/spring-boot-kafka-json-message-example-0.0.1-SNAPSHOT-exec.jar &