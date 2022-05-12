package io.baris.petclinic.vertxkafka.kafka;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.MY_TOPIC;
import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getBootstrapServers;

/**
 * Publishes events to the kafka topics
 */
@Slf4j
public class KafkaPublisher {

    private final KafkaProducer<EventType, String> producer;

    public KafkaPublisher(final Vertx vertx) {
        this.producer = KafkaProducer.create(vertx, getKafkaProducerConfig());
    }

    public void publish(
        final EventType eventType,
        final String value
    ) {
        var record = new KafkaProducerRecordImpl<>(MY_TOPIC, eventType, value);
        producer.send(record);
        log.info("Event sent with key={}, value={}", eventType, value);
    }

    private Map<String, String> getKafkaProducerConfig() {
        return Map.of(
            "bootstrap.servers", getBootstrapServers(),
            "key.serializer", "io.baris.petclinic.vertxkafka.kafka.EventTypeSerializer",
            "value.serializer", "org.apache.kafka.common.serialization.StringSerializer",
            "acks", "1"
        );
    }
}
