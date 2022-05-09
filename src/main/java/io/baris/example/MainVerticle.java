package io.baris.example;

import io.baris.example.kafka.KafkaPublisher;
import io.baris.example.kafka.KafkaSubscriber;
import io.baris.example.pet.PetController;
import io.baris.example.pet.PetManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

    public static final String KAFKA_URL = "kafka:9092";
    public static final String MY_TOPIC = "mytopic";

    public static void main(final String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var kafkaVertxPublisher = new KafkaPublisher(vertx);
        var petManager = new PetManager(kafkaVertxPublisher);
        new KafkaSubscriber(vertx, petManager);

        //var petManager = new PetManager(null);
        var petController = new PetController(petManager);

        var router = Router.router(vertx);
        router.route("/pets*").handler(BodyHandler.create());

        router.put("/pets").handler(petController::createPet);
        router.get("/pets").handler(petController::getAllPets);
        router.get("/pets/:pet_id").handler(petController::getPet);
        router.post("/pets/:pet_id").handler(petController::updatePet);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
            .onSuccess(server ->
                log.info("Server started at port {}!", server.actualPort())
            );
    }
}
