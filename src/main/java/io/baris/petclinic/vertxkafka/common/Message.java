package io.baris.petclinic.vertxkafka.common;

import lombok.Builder;
import lombok.Value;

/**
 * Message class to return messages
 */
@Value
@Builder
public class Message {

    String message;
}
