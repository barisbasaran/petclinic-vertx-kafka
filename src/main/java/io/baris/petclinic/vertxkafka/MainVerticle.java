package io.baris.petclinic.vertxkafka;

import io.baris.petclinic.vertxkafka.kafka.KafkaPublisher;
import io.baris.petclinic.vertxkafka.kafka.KafkaSubscriber;
import io.baris.petclinic.vertxkafka.pet.PetEventPublisher;
import io.baris.petclinic.vertxkafka.pet.PetManager;
import io.baris.petclinic.vertxkafka.pet.PetResource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

import static io.vertx.core.Future.succeededFuture;

/**
 * Main class to launch the application
 */
@Slf4j
public class MainVerticle extends AbstractVerticle {

    public static void main(final String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var kafkaVertxPublisher = new KafkaPublisher(vertx);
        var petEventPublisher = new PetEventPublisher(kafkaVertxPublisher);
        var petManager = new PetManager();
        new KafkaSubscriber(vertx, petManager);

        var petResource = new PetResource(petManager, petEventPublisher);

        var router = Router.router(vertx);
        router.route("/pets*").handler(BodyHandler.create());

        router.get("/").respond(context -> succeededFuture("Welcome to Pet Clinic"));
        router.put("/pets").handler(petResource::createPet);
        router.get("/pets").handler(petResource::getAllPets);
        router.get("/pets/:pet_id").handler(petResource::getPet);
        router.post("/pets/:pet_id").handler(petResource::updatePet);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
            .onSuccess(server ->
                log.info("Server started at port {}!", server.actualPort())
            );
    }
}
