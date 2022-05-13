package io.baris.petclinic.vertxkafka.kafka;

import io.baris.petclinic.vertxkafka.pet.PetManager;
import io.baris.petclinic.vertxkafka.pet.PetMapper;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;

import java.util.Map;

import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getTopic;

/**
 * Subscribes to kafka topics to receive events
 */
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final KafkaConsumer<EventType, String> kafkaConsumer;
    private final PetManager petManager;

    public static Consumer<EventType, String> getCoreConsumer() {
        return new org.apache.kafka.clients.consumer.KafkaConsumer<>(getKafkaConsumerConfig());
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
            "bootstrap.servers", getBootstrapServers(),
            "key.deserializer", "io.baris.petclinic.vertxkafka.kafka.EventTypeDeserializer",
            "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
            "group.id", "test",
            "auto.offset.reset", "earliest",
            "enable.auto.commit", "true",
            "auto.commit.interval.ms", "1000",
            "session.timeout.ms", "30000"
        );
    }
}
