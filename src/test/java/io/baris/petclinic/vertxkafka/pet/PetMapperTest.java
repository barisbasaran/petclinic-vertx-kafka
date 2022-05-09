package io.baris.petclinic.vertxkafka.pet;

import io.baris.petclinic.vertxkafka.pet.model.Species;
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
        assertThat(createPet.getName()).isEqualTo("Sofi");
        assertThat(createPet.getAge()).isEqualTo(2);
        assertThat(createPet.getSpecies()).isEqualTo(Species.CAT);
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
        assertThat(updatePet.getId()).isEqualTo(21);
        assertThat(updatePet.getName()).isEqualTo("Sofi");
        assertThat(updatePet.getAge()).isEqualTo(2);
        assertThat(updatePet.getSpecies()).isEqualTo(Species.CAT);
    }
}
