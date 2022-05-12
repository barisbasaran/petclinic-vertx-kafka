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
public class PetResource {

    private final PetManager petManager;
    private final PetEventPublisher petEventPublisher;

    public void createPet(final RoutingContext context) {
        petEventPublisher.publishCreatePet(context.getBodyAsString());

        context.json(new Message("Pet create request accepted"));
    }

    public void updatePet(final RoutingContext context) {
        var json = context.getBodyAsJson();
        json.put("id", getPetId(context));
        petEventPublisher.publishUpdatePet(json.toString());

        context.json(new Message("Pet update request accepted"));
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
