package com.netsurfingzone.config;

import java.util.HashMap;
import java.util.Map;
import com.netsurfingzone.dto.Notify;
import com.netsurfingzone.dto.SummaryPayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.netsurfingzone.constant.ApplicationConstant;

@Configuration
@EnableKafka
public class SpringKafkaConfig {

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ApplicationConstant.KAFKA_LOCAL_SERVER_CONFIG);
		configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES,"com.netsurfingzone.dto.Notify");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES,"com.netsurfingzone.dto.SummaryPayload");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
		configMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);
		return new DefaultKafkaProducerFactory<String, Object>(configMap);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, Notify> consumerFactory() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ApplicationConstant.KAFKA_LOCAL_SERVER_CONFIG);//mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		configMap.put(ConsumerConfig.GROUP_ID_CONFIG, ApplicationConstant.GROUP_ID_JSON);
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto.Notify");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//		configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"700000");
//		configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
//		configMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
//		configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,"3600000");
//		configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"10");
//		configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
		//configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaNotifyDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configMap);
	}

	@Bean
	public ConsumerFactory<String, SummaryPayload> consumerFactory1() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ApplicationConstant.KAFKA_LOCAL_SERVER_CONFIG);//mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		configMap.put(ConsumerConfig.GROUP_ID_CONFIG, ApplicationConstant.GROUP_ID_JSON);
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto.SummaryPayload");
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//		configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"700000");
//		configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
//		configMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
//		configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,"3600000");
//		configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"10");
//		configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
		//configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaNotifyDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configMap);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Notify> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Notify> factory = new ConcurrentKafkaListenerContainerFactory<String, Notify>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, SummaryPayload> kafkaListenerContainerFactory1() {
		ConcurrentKafkaListenerContainerFactory<String, SummaryPayload> factory = new ConcurrentKafkaListenerContainerFactory<String, SummaryPayload>();
		factory.setConsumerFactory(consumerFactory1());
		return factory;
	}
}
