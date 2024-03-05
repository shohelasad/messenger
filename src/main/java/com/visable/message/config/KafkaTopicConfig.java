package com.visable.message.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Value(value = "${spring.kafka.topic.name}")
    private String messageTopic;

    @Value(value = "${spring.kafka.topic.partitions}")
    private int partitions;

    @Value(value = "${spring.kafka.topic.replicas}")
    private int replicas;

    @Bean
    public NewTopic messageTopic() {
        return TopicBuilder.name(messageTopic)
               .partitions(partitions)
               .replicas(replicas)
               .build();
    }
}
