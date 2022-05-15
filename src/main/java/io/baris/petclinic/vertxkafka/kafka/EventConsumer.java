package io.baris.petclinic.vertxkafka.kafka;

import io.baris.petclinic.vertxkafka.pet.PetManager;
import io.baris.petclinic.vertxkafka.pet.PetMapper;
import io.baris.petclinic.vertxkafka.system.ApplicationConfig;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.MockConsumer;

import java.util.Map;

import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getTopic;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.consumer.OffsetResetStrategy.NONE;

/**
 * Subscribes to kafka topics to receive events
 */
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final KafkaConsumer<EventType, String> kafkaConsumer;
    private final PetManager petManager;

    public static Consumer<EventType, String> createKafkaConsumer(
        final ApplicationConfig applicationConfig
    ) {
        return applicationConfig.testEnv()
            ? new MockConsumer<>(NONE)
            : new org.apache.kafka.clients.consumer.KafkaConsumer<>(getKafkaConsumerConfig());
    }

    public void subscribe() {
        this.kafkaConsumer.subscribe(getTopic());

        this.kafkaConsumer.handler(record -> {
            log.info("Event received for key={}, partition={}, offset={}, value={}",
                record.key(), record.partition(), record.offset(), record.value()
            );
            switch (record.key()) {
                case CREATE_PET -> petManager.createPet(PetMapper.mapToCreatePet(record.value()));
                case UPDATE_PET -> petManager.updatePet(PetMapper.mapToUpdatePet(record.value()));
                default -> log.info("No service found for key={}", record.key());
            }
        });
    }

    private static Map<String, Object> getKafkaConsumerConfig() {
        return Map.of(
            BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers(),
            KEY_DESERIALIZER_CLASS_CONFIG, "io.baris.petclinic.vertxkafka.kafka.EventTypeDeserializer",
            VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer",
            GROUP_ID_CONFIG, "test",
            AUTO_OFFSET_RESET_CONFIG, "earliest",
            ENABLE_AUTO_COMMIT_CONFIG, "true",
            AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000",
            SESSION_TIMEOUT_MS_CONFIG, "30000"
        );
    }
}
