package com.netsurfingzone.constant;

public class ApplicationConstant {

	public static final String KAFKA_LOCAL_SERVER_CONFIG = "localhost:9092";
	public static final String TOPIC_NAME = "netsurfingzone-topic-1";
	public static final String TOPIC_NAME_RF_TEMPLATE = "netsurfingzone-topic-2";
	public static final String TOPIC_NAME_SUMMARY = "netsurfingzone-topic-1-summary";
	public static final String TOPIC_NAME_SUMMARY_ATTACHMENTS = "netsurfingzone-topic-2-summary";
	public static final String KAFKA_LISTENER_CONTAINER_FACTORY = "kafkaListenerContainerFactory";
	public static final String GROUP_ID_JSON = "#{T(java.util.UUID).randomUUID().toString()}";//"group-id-json-1";

}
