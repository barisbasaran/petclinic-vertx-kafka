package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.pet.model.CreatePet;
import io.baris.petclinic.vertxkafka.pet.model.Pet;
import io.baris.petclinic.vertxkafka.pet.model.UpdatePet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.baris.petclinic.vertxkafka.pet.PetMapper.mapToPet;
import static java.util.function.Predicate.isEqual;

/**
 * Manages pets
 */
@RequiredArgsConstructor
@Slf4j
public class PetManager {

    private final Map<Integer, Pet> pets = new HashMap<>();

    public void createPet(final CreatePet createPet) {
        var id = getLatestPetId() + 1;
        var pet = mapToPet(createPet, id);

        pets.put(id, pet);
        log.info("Created pet {}", pet);
    }

    public void updatePet(UpdatePet updatePet) {
        var petId = updatePet.getId();
        Optional
            .ofNullable(pets.get(petId))
            .ifPresentOrElse(it -> {
                    var pet = mapToPet(updatePet);
                    pets.put(petId, pet);
                    log.info("Updated pet as {}", pet);
                },
                () -> log.info("Pet with id {} not found", petId)
            );
    }

    public Optional<Pet> getPet(final int petId) {
        return pets.keySet()
            .stream()
            .filter(isEqual(petId))
            .findFirst()
            .map(pets::get);
    }

    public List<Pet> getAllPets() {
        return pets.values()
            .stream()
            .toList();
    }

    private int getLatestPetId() {
        return pets.keySet()
            .stream()
            .mapToInt(v -> v)
            .max()
            .orElse(0);
    }
}
