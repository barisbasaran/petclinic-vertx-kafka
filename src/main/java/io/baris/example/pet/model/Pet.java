package io.baris.example.pet.model;

import lombok.Builder;
import lombok.Value;

/**
 * Represents a pet
 */

@Builder
@Value
public class Pet {

    int id;
    String name;
    int age;
    Species species;
}
