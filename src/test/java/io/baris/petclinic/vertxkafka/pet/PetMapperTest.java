package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.pet.model.Species;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetMapperTest {

    @Test
    void mapToCreatePet() {
        // act
        var createPet = PetMapper.mapToCreatePet("""
            {
                "name": "Sofi",
                "age": 2,
                "species": "CAT"
            }           
            """);

        // assert
        Assertions.assertThat(createPet.getName()).isEqualTo("Sofi");
        Assertions.assertThat(createPet.getAge()).isEqualTo(2);
        Assertions.assertThat(createPet.getSpecies()).isEqualTo(Species.CAT);
    }

    @Test
    void mapToUpdatePet() {
        // act
        var updatePet = PetMapper.mapToUpdatePet("""
            {
                "id": 21,
                "name": "Sofi",
                "age": 2,
                "species": "CAT"
            }           
            """);

        // assert
        Assertions.assertThat(updatePet.getId()).isEqualTo(21);
        Assertions.assertThat(updatePet.getName()).isEqualTo("Sofi");
        Assertions.assertThat(updatePet.getAge()).isEqualTo(2);
        Assertions.assertThat(updatePet.getSpecies()).isEqualTo(Species.CAT);
    }
}
