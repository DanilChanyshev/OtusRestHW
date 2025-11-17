package rest.pet;

import PetsStore.Enum.Category;
import PetsStore.Enum.StatusPet;
import PetsStore.Enum.Tags;
import PetsStore.dto.PetDTO;
import PetsStore.dto.PetResponseDTO;
import PetsStore.dto.TagDTO;
import PetsStore.service.PetsStoreApi;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.containsString;

public class FindPetByIdTest {

    List<String> photoUrl = Collections.singletonList("svgPhoto1");
    List<TagDTO> tags = List.of(Tags.DOG_TAG.getTag(), Tags.COLOR_WHITE_TAG.getTag());
    Random random = new Random(12345L);
    private PetsStoreApi petsStoreApi;

    @BeforeEach
    void setUp() {
        petsStoreApi = new PetsStoreApi();
        ((Logger) LoggerFactory.getLogger("org.apache.http")).setLevel(Level.INFO);
        ((Logger) LoggerFactory.getLogger("io.restassured")).setLevel(Level.INFO);
    }

    @Test
    @DisplayName("Проверка поиска созданного питомца по его id")
    void findPetByIdTest() {

        final PetDTO newDog = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.DOG.getCategory())
                .name("Hasky")
                .photoUrls(photoUrl)
                .tags(tags)
                .status(StatusPet.AVAILABLE.getTitle())
                .build();

        petsStoreApi.addNewPet(newDog)
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(PetResponseDTO.class);

        petsStoreApi.petFindByPetId(newDog.getId())
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreatePetSchema.json"));
    }

    @Test
    @DisplayName("Проверка поиска удаленного питомца по его id")
    void findDeletedPetByIdTest() {

        final PetDTO cat = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.CAT.getCategory())
                .name("Marfa")
                .photoUrls(photoUrl)
                .tags(List.of(Tags.CAT_TAG.getTag()))
                .status(StatusPet.PENDING.getTitle())
                .build();

        petsStoreApi.addNewPet(cat)
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(PetResponseDTO.class);

        petsStoreApi.petDeleteByPetId(cat.getId())
                .statusCode(HttpStatus.SC_OK);

        petsStoreApi.petFindByPetId(cat.getId())
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("type", containsString("error"))
                .body("message", containsString("Pet not found"));
    }

    @Test
    @DisplayName("Проверка поиска питомца по несуществующему id")
    void findPetByNotValidId() {

        petsStoreApi.petFindByPetId(-2L)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("type", containsString("error"))
                .body("message", containsString("Pet not found"));
    }

}
