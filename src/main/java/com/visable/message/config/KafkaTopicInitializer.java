package com.visable.message.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class KafkaTopicInitializer implements CommandLineRunner {
    @Value(value = "${kafka.bootstrap.servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.topic.name}")
    private String messageTopic;

    @Override
    public void run(String... args) throws Exception {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        try (AdminClient adminClient = AdminClient.create(properties)) {
            adminClient.createTopics(Collections.singleton(
                    new NewTopic(messageTopic, 1, (short) 1))).all().get();
            log.info("Topic " + messageTopic +  " created successfully.");

        } catch (ExecutionException | InterruptedException e) {
        }
    }
}
