package io.baris.petclinic.vertxkafka.common;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Message {

    String message;
}
