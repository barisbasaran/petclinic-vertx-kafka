package io.baris.petclinic.vertxkafka;

import io.baris.petclinic.vertxkafka.kafka.EventConsumer;
import io.baris.petclinic.vertxkafka.kafka.EventProducer;
import io.baris.petclinic.vertxkafka.pet.PetEventProducer;
import io.baris.petclinic.vertxkafka.pet.PetManager;
import io.baris.petclinic.vertxkafka.pet.PetResource;
import io.baris.petclinic.vertxkafka.system.ApplicationConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.baris.petclinic.vertxkafka.kafka.EventConsumer.createKafkaConsumer;
import static io.baris.petclinic.vertxkafka.kafka.EventProducer.createKafkaProducer;
import static io.vertx.core.Future.succeededFuture;

/**
 * Main class to launch the application
 */
@Slf4j
@RequiredArgsConstructor
public class MainVerticle extends AbstractVerticle {

    public static void main(final String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    private final ApplicationConfig applicationConfig;

    public MainVerticle() {
        this.applicationConfig = ApplicationConfig.builder()
            .env("prod")
            .build();
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var petManager = new PetManager();
        var kafkaConsumer = KafkaConsumer.create(vertx, createKafkaConsumer(applicationConfig));
        new EventConsumer(kafkaConsumer, petManager)
            .subscribe();

        var kafkaProducer = KafkaProducer.create(vertx, createKafkaProducer(applicationConfig));
        var eventProducer = new EventProducer(kafkaProducer);
        var petEventProducer = new PetEventProducer(eventProducer);
        var petResource = new PetResource(petManager, petEventProducer);

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
            .onSuccess(server -> {
                log.info("Server started at port {}!", server.actualPort());
                startPromise.complete();
            });
    }
}
