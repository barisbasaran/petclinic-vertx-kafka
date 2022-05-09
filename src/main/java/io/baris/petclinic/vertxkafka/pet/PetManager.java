package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.kafka.KafkaPublisher;
import io.baris.petclinic.vertxkafka.pet.model.CreatePet;
import io.baris.petclinic.vertxkafka.pet.model.Pet;
import io.baris.petclinic.vertxkafka.pet.model.UpdatePet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.baris.petclinic.vertxkafka.kafka.EventType.CREATE_PET;
import static io.baris.petclinic.vertxkafka.kafka.EventType.UPDATE_PET;
import static io.baris.petclinic.vertxkafka.pet.PetMapper.mapToPet;
import static java.util.function.Predicate.isEqual;

/**
 * Manages pets
 */
@RequiredArgsConstructor
@Slf4j
public class PetManager {

    private final Map<Integer, Pet> pets = new HashMap<>();

    private final KafkaPublisher kafkaPublisher;

    public void publishCreatePet(final String jsonStr) {
        kafkaPublisher.publish(CREATE_PET, jsonStr);
    }

    public void publishUpdatePet(final String jsonStr) {
        kafkaPublisher.publish(UPDATE_PET, jsonStr);
    }

    public void createPet(final CreatePet createPet) {
        var id = getMaximumId() + 1;
        var pet = mapToPet(createPet, id);
        pets.put(id, pet);
        log.info("Created pet {}", pet);
    }

    public void updatePet(UpdatePet updatePet) {
        var pet = mapToPet(updatePet);
        pets.put(updatePet.getId(), pet);
        log.info("Updated pet {}", pet);
    }

    public Optional<Pet> getPet(final int petId) {
        return pets.keySet().stream()
            .filter(isEqual(petId))
            .findFirst()
            .map(pets::get);
    }

    public List<Pet> getAllPets() {
        return pets.values().stream().toList();
    }

    private int getMaximumId() {
        return pets.keySet().stream().mapToInt(v -> v).max().orElse(0);
    }
}
