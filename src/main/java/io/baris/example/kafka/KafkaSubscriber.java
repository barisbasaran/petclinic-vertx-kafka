package io.baris.example.kafka;

import io.baris.example.pet.PetManager;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static io.baris.example.MainVerticle.KAFKA_URL;
import static io.baris.example.MainVerticle.MY_TOPIC;
import static io.baris.example.pet.PetMapper.mapToCreatePet;
import static io.baris.example.pet.PetMapper.mapToUpdatePet;

@Slf4j
public class KafkaSubscriber {

    private final KafkaConsumer<EventType, String> consumer;

    private final PetManager petManager;

    public KafkaSubscriber(
        final Vertx vertx,
        final PetManager petManager
    ) {
        this.consumer = KafkaConsumer.create(vertx, getKafkaConsumerConfig());
        this.petManager = petManager;

        subscribeServices();
    }

    private void subscribeServices() {
        this.consumer.subscribe(MY_TOPIC);

        this.consumer.handler(record -> {
            log.info("Processing key={},value={}, partition={},offset={}",
                record.key(), record.value(), record.partition(), record.offset()
            );
            switch (record.key()) {
                case CREATE_PET -> petManager.createPet(mapToCreatePet(record.value()));
                case UPDATE_PET -> petManager.updatePet(mapToUpdatePet(record.value()));
                default -> log.info("No service found for key={}", record.key());
            }
        });
    }

    private Map<String, String> getKafkaConsumerConfig() {
        return Map.of(
            "bootstrap.servers", KAFKA_URL,
            "key.deserializer", "io.baris.example.kafka.EventTypeDeserializer",
            "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
            "group.id", "test",
            "auto.offset.reset", "earliest",
            "enable.auto.commit", "true",
            "auto.commit.interval.ms", "1000",
            "session.timeout.ms", "30000"
        );
    }
}
