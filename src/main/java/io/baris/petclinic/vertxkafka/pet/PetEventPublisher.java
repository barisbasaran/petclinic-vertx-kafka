package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.kafka.KafkaPublisher;
import lombok.RequiredArgsConstructor;

import static io.baris.petclinic.vertxkafka.kafka.EventType.CREATE_PET;
import static io.baris.petclinic.vertxkafka.kafka.EventType.UPDATE_PET;

/**
 * Publishes events for pets
 */
@RequiredArgsConstructor
public class PetEventPublisher {

    private final KafkaPublisher kafkaPublisher;

    public void publishCreatePet(final String jsonStr) {
        kafkaPublisher.publish(CREATE_PET, jsonStr);
    }

    public void publishUpdatePet(final String jsonStr) {
        kafkaPublisher.publish(UPDATE_PET, jsonStr);
    }
}
