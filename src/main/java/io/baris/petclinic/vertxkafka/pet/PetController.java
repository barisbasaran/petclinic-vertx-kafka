package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.common.Message;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for pets
 */
@RequiredArgsConstructor
@Slf4j
public class PetController {

    private final PetManager petManager;

    public void createPet(final RoutingContext context) {
        petManager.publishCreatePet(context.getBodyAsString());

        context.json(Message.builder()
            .message("Pet create request accepted")
            .build()
        );
    }

    public void updatePet(final RoutingContext context) {
        var json = context.getBodyAsJson();
        json.put("id", getPetId(context));
        petManager.publishUpdatePet(json.toString());

        context.json(Message.builder()
            .message("Pet update request accepted")
            .build()
        );
    }

    public void getPet(final RoutingContext context) {
        petManager.getPet(getPetId(context))
            .map(context::json)
            .orElseGet(() -> context.response()
                .setStatusCode(404)
                .end("Pet not found")
            );
    }

    public void getAllPets(final RoutingContext context) {
        context.json(petManager.getAllPets());
    }

    private int getPetId(final RoutingContext context) {
        return Integer.parseInt(context.pathParam("pet_id"));
    }
}
