package io.baris.petclinic.vertxkafka.kafka;

import org.apache.kafka.common.serialization.Serializer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EventTypeSerializer implements Serializer<EventType> {

    @Override
    public byte[] serialize(String topic, EventType data) {
        return data.toString().getBytes(UTF_8);
    }
}
