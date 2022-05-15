package io.baris.petclinic.vertxkafka.kafka;

import io.baris.petclinic.vertxkafka.system.ApplicationConfig;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;

import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.petclinic.vertxkafka.kafka.KafkaUtils.getTopic;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

/**
 * Publishes events to the kafka topics
 */
@Slf4j
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaProducer<EventType, String> kafkaProducer;

    public static Producer<EventType, String> createKafkaProducer(
        final ApplicationConfig applicationConfig
    ) {
        return applicationConfig.testEnv()
            ? new MockProducer<>(false, new EventTypeSerializer(), new StringSerializer())
            : new org.apache.kafka.clients.producer.KafkaProducer<>(getKafkaProducerConfig());
    }

    public void publishEvent(
        final EventType eventType,
        final String value
    ) {
        var record = new KafkaProducerRecordImpl<>(getTopic(), eventType, value);
        kafkaProducer.send(record);
        log.info("Event sent with key={}, value={}", eventType, value);
    }

    private static Map<String, Object> getKafkaProducerConfig() {
        return Map.of(
            BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers(),
            KEY_SERIALIZER_CLASS_CONFIG, "io.baris.petclinic.vertxkafka.kafka.EventTypeSerializer",
            VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer",
            ACKS_CONFIG, "1"
        );
    }
}
