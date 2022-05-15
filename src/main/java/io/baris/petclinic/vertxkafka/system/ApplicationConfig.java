package io.baris.petclinic.vertxkafka.system;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApplicationConfig {

    private String env;

    public boolean testEnv() {
        return "test".equalsIgnoreCase(env);
    }
}
