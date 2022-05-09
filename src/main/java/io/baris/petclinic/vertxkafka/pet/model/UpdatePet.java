package io.baris.petclinic.vertxkafka.pet.model;

import lombok.Builder;
import lombok.Value;

/**
 * Model for updating a pet
 */

@Builder
@Value
public class UpdatePet {

    int id;
    String name;
    int age;
    Species species;
}
