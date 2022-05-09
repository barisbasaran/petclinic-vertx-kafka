package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.pet.model.CreatePet;
import io.baris.petclinic.vertxkafka.pet.model.Pet;
import io.baris.petclinic.vertxkafka.pet.model.UpdatePet;
import io.vertx.core.json.JsonObject;

public class PetMapper {

    public static CreatePet mapToCreatePet(final String json) {
        return new JsonObject(json).mapTo(CreatePet.class);
    }

    public static UpdatePet mapToUpdatePet(final String json) {
        return new JsonObject(json).mapTo(UpdatePet.class);
    }

    public static Pet mapToPet(
        final CreatePet createPet,
        final int id
    ) {
        return Pet.builder()
            .id(id)
            .name(createPet.getName())
            .species(createPet.getSpecies())
            .age(createPet.getAge())
            .build();
    }

    public static Pet mapToPet(final UpdatePet updatePet) {
        return Pet.builder()
            .id(updatePet.getId())
            .name(updatePet.getName())
            .species(updatePet.getSpecies())
            .age(updatePet.getAge())
            .build();
    }
}
