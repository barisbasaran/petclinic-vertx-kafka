package io.baris.petclinic.vertxkafka.kafka;

import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;

import java.util.Map;

import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getTopic;

/**
 * Publishes events to the kafka topics
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaPublisher {

    private final KafkaProducer<EventType, String> kafkaProducer;

    public static Producer<EventType, String> getCoreProducer() {
        return new org.apache.kafka.clients.producer.KafkaProducer<>(getKafkaProducerConfig());
    }

    public void publish(
        final EventType eventType,
        final String value
    ) {
        var record = new KafkaProducerRecordImpl<>(getTopic(), eventType, value);
        kafkaProducer.send(record);
        log.info("Event sent with key={}, value={}", eventType, value);
    }

    private static Map<String, Object> getKafkaProducerConfig() {
        return Map.of(
            "bootstrap.servers", getBootstrapServers(),
            "key.serializer", "io.baris.petclinic.vertxkafka.kafka.EventTypeSerializer",
            "value.serializer", "org.apache.kafka.common.serialization.StringSerializer",
            "acks", "1"
        );
    }
}
