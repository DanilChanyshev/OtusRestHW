package rest.pet;

import PetsStore.Enum.Category;
import PetsStore.Enum.StatusPet;
import PetsStore.Enum.Tags;
import PetsStore.dto.CategoryDTO;
import PetsStore.dto.PetDTO;
import PetsStore.dto.PetResponseDTO;
import PetsStore.dto.TagDTO;
import PetsStore.service.PetsStoreApi;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.containsString;

public class CreatePetTest {

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
    @DisplayName("RespApi. Проверка создания питомца. Проверка возвращаемых данных")
    void createNewPetTest() {

        final PetDTO newDog = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.DOG.getCategory())
                .name("Hasky")
                .photoUrls(photoUrl)
                .tags(tags)
                .status(StatusPet.AVAILABLE.getTitle())
                .build();

        PetResponseDTO actual = petsStoreApi.addNewPet(newDog)
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(PetResponseDTO.class);

        CategoryDTO expectedCategory = Category.DOG.getCategory();
        String expectedStatus = StatusPet.AVAILABLE.getTitle();
        List<String> expectedPhotoUrl = photoUrl;
        List<TagDTO> expectedTags = tags;

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedCategory, actual.getCategory(), "Incorrect category"),
                () -> Assertions.assertEquals(newDog.getName(), actual.getName(), "Incorrect name"),
                () -> Assertions.assertEquals(expectedStatus, actual.getStatus(), "Incorrect status"),
                () -> Assertions.assertEquals(expectedPhotoUrl, actual.getPhotoUrls(), "Incorrect photo url"),
                () -> Assertions.assertEquals(newDog.getId(), actual.getId(), "Incorrect id"),
                () -> Assertions.assertEquals(expectedTags, actual.getTags(), "Incorrect tag's")
        );
    }

    @Test
    @DisplayName("RespApi. Проверка создания питомца без обязательных полей.")
    void createPetWithoutRequiredFieldTest() {

        final PetDTO invalidPet = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.DOG.getCategory())
                .tags(tags)
                .status(StatusPet.AVAILABLE.getTitle())
                .build();

        petsStoreApi.addNewPet(invalidPet)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString("Invalid input"));
    }


    @Test
    @DisplayName("RespApi. Проверка создания питомца. Валидация полученных данных по схеме")
    void checkDataCreatePetToSchemaTest() {

        final PetDTO newDog = PetDTO.builder()
                .id(random.nextLong())
                .category(Category.CAT.getCategory())
                .name("Ugol")
                .photoUrls(photoUrl)
                .tags(List.of(Tags.CAT_TAG.getTag()))
                .status(StatusPet.PENDING.getTitle())
                .build();

        petsStoreApi.addNewPet(newDog)
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreatePetSchema.json"));
    }
}
