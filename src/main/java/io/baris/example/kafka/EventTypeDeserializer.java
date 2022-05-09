package io.baris.example.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EventTypeDeserializer implements Deserializer<EventType> {

    @Override
    public EventType deserialize(String topic, byte[] data) {
        return EventType.valueOf(new String(data, UTF_8));
    }
}
