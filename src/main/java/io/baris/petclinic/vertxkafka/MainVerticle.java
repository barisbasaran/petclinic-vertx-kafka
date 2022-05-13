package io.baris.petclinic.vertxkafka;

import io.baris.petclinic.vertxkafka.kafka.EventType;
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
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

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

    private final Producer<EventType, String> kafkaProducer;
    private final Consumer<EventType, String> kafkaConsumer;

    public MainVerticle() {
        this.kafkaProducer = KafkaPublisher.getCoreProducer();
        this.kafkaConsumer = KafkaSubscriber.getCoreConsumer();
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var kafkaVertxPublisher = new KafkaPublisher(KafkaProducer.create(vertx, kafkaProducer));
        var petEventPublisher = new PetEventPublisher(kafkaVertxPublisher);
        var petManager = new PetManager();
        var kafkaSubscriber = new KafkaSubscriber(KafkaConsumer.create(vertx, kafkaConsumer), petManager);
        kafkaSubscriber.subscribeServices();

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
            .onSuccess(server -> {
                log.info("Server started at port {}!", server.actualPort());
                startPromise.complete();
            });
    }
}
