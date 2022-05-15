package io.baris.petclinic.vertxkafka;

import io.baris.petclinic.vertxkafka.system.ApplicationConfig;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.vertx.core.http.HttpMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HomepageTest {

    @BeforeEach
    public void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        var applicationConfig = ApplicationConfig.builder()
            .env("test")
            .build();
        var verticle = new MainVerticle(applicationConfig);
        vertx.deployVerticle(verticle, testContext.succeedingThenComplete());
    }

    @Test
    public void homepage(Vertx vertx, VertxTestContext testContext) {
        vertx.createHttpClient()
            .request(GET, 8080, "localhost", "/")
            .compose(HttpClientRequest::send)
            .compose(HttpClientResponse::body)
            .onSuccess(body -> testContext.verify(() -> {
                // Check the response
                assertThat(body.toString()).isEqualTo("Welcome to Pet Clinic");
                testContext.completeNow();
            }));
    }
}
