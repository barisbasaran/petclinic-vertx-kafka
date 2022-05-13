package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.kafka.EventProducer;
import lombok.RequiredArgsConstructor;

import static io.baris.petclinic.vertxkafka.kafka.EventType.CREATE_PET;
import static io.baris.petclinic.vertxkafka.kafka.EventType.UPDATE_PET;

/**
 * Publishes events for pets
 */
@RequiredArgsConstructor
public class PetEventProducer {

    private final EventProducer eventProducer;

    public void publishCreatePet(final String jsonStr) {
        eventProducer.publishEvent(CREATE_PET, jsonStr);
    }

    public void publishUpdatePet(final String jsonStr) {
        eventProducer.publishEvent(UPDATE_PET, jsonStr);
    }
}
