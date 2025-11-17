package rest.pet;

import PetsStore.Enum.Category;
import PetsStore.Enum.StatusPet;
import PetsStore.Enum.Tags;
import PetsStore.dto.PetDTO;
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
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

public class FindPetByStatusTest {

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
    @DisplayName("RestApi. Проверка поиска питомца по статусу. Проверка заполненности полей")
    void petFindByStatus() {
        final PetDTO newDog = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.DOG.getCategory())
                .name("Hasky")
                .photoUrls(photoUrl)
                .tags(tags)
                .status(StatusPet.AVAILABLE.getTitle())
                .build();
        petsStoreApi.addNewPet(newDog)
                .statusCode(HttpStatus.SC_OK);

        petsStoreApi.petFindByStatus(StatusPet.AVAILABLE)
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("category", notNullValue())
                .body("name", notNullValue())
                .body("photoUrls", notNullValue())
                .body("tags", notNullValue())
                .body("status", containsString(StatusPet.AVAILABLE.getTitle()));
    }

    @Test
    @DisplayName("RestApi. Проверка поиска питомца по статусу. Проверка времени отработки")
    void checkTimeLimit() {
        final PetDTO newDog = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.DOG.getCategory())
                .name("Hasky")
                .photoUrls(photoUrl)
                .tags(tags)
                .status(StatusPet.SOLD.getTitle())
                .build();
        petsStoreApi.addNewPet(newDog)
                .statusCode(HttpStatus.SC_OK);

        petsStoreApi.petFindByStatus(StatusPet.SOLD)
                .statusCode(HttpStatus.SC_OK)
                .time(lessThan(1500L));
    }

    @Test
    @DisplayName("RestApi. Проверка поиска питомца по статусу. Проверка полученных данных по схеме")
    void checkMessageToSchema() {

        final PetDTO newDog = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.DOG.getCategory())
                .name("Hasky")
                .photoUrls(photoUrl)
                .tags(tags)
                .status(StatusPet.PENDING.getTitle())
                .build();
        petsStoreApi.addNewPet(newDog)
                .statusCode(HttpStatus.SC_OK);

        petsStoreApi.petFindByStatus(StatusPet.PENDING)
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/FindPet.json"));
    }
}
