package io.baris.petclinic.vertxkafka.pet;

import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.baris.petclinic.vertxkafka.system.WebUtils.accepted;
import static io.baris.petclinic.vertxkafka.system.WebUtils.pathParamAsInt;

/**
 * Controller class for pets
 */
@RequiredArgsConstructor
@Slf4j
public class PetResource {

    public static final String PET_ID = "pet_id";

    private final PetManager petManager;
    private final PetEventPublisher petEventPublisher;

    public void createPet(final RoutingContext context) {
        var jsonStr = context.getBodyAsString();
        petEventPublisher.publishCreatePet(jsonStr);

        accepted(context, "Pet create request accepted");
    }

    public void updatePet(final RoutingContext context) {
        var json = context.getBodyAsJson();
        var petId = pathParamAsInt(context, PET_ID);
        json.put("id", petId);

        petEventPublisher.publishUpdatePet(json.toString());

        accepted(context, "Pet update request accepted");
    }

    public void getPet(final RoutingContext context) {
        var petId = pathParamAsInt(context, PET_ID);
        petManager.getPet(petId)
            .map(context::json)
            .orElseGet(() -> context.response()
                .setStatusCode(404)
                .end("Pet not found")
            );
    }

    public void getAllPets(final RoutingContext context) {
        context.json(petManager.getAllPets());
    }
}
