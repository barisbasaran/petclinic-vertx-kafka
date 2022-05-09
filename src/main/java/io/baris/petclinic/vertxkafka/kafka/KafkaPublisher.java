package io.baris.petclinic.vertxkafka.kafka;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.KAFKA_URL;
import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.MY_TOPIC;

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
        log.info("Message sent successfully");
    }

    private Map<String, String> getKafkaProducerConfig() {
        return Map.of(
            "bootstrap.servers", KAFKA_URL,
            "key.serializer", "io.baris.petclinic.vertxkafka.kafka.EventTypeSerializer",
            "value.serializer", "org.apache.kafka.common.serialization.StringSerializer",
            "acks", "1"
        );
    }
}
