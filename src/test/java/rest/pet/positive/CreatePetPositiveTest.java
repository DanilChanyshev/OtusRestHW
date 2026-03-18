package rest.pet.positive;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import petsstore.dto.CategoryDTO;
import petsstore.dto.PetDTO;
import petsstore.dto.PetResponseDTO;
import petsstore.dto.TagDTO;
import petsstore.model.Category;
import petsstore.model.StatusPet;
import petsstore.model.Tags;
import rest.pet.BasePetApiTest;
import wiremock.stubs.PetStubs;
import java.util.Collections;
import java.util.List;

public class CreatePetPositiveTest extends BasePetApiTest {

  private static final List<String> PHOTO_URL = Collections.singletonList("svgPhoto1");
  private static final List<TagDTO> PET_TAG = List.of(
          Tags.DOG_TAG.getTag(),
          Tags.COLOR_WHITE_TAG.getTag());
  private static final String PET_SCHEMA = "schema/CreatePetSchema.json";

  @SneakyThrows
  @Test
  @Epic("Pet API")
  @Feature("Create pet")
  @Story("Create a new pet successfully")
  @DisplayName("RespApi. Проверка создания питомца. Проверка возвращаемых данных")
  void createNewPetTest() {
    PetDTO newDog = PetDTO.builder()
            .id(RANDOM.nextLong())
            .category(Category.DOG.getCategory())
            .name("Hasky")
            .photoUrls(PHOTO_URL)
            .tags(PET_TAG)
            .status(StatusPet.AVAILABLE.getTitle())
            .build();

    PetStubs.stubCreatePet(wireMockServer, newDog);
    PetStubs.stubGetPetById(wireMockServer, newDog);

    Response response = petsStoreService.addNewPet(newDog)
            .statusCode(HttpStatus.SC_OK)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PET_SCHEMA))
            .extract().response();

    petId = newDog.getId();

    PetResponseDTO actualResponse = response.as(PetResponseDTO.class);

    CategoryDTO expectedCategory = Category.DOG.getCategory();
    String expectedStatus = StatusPet.AVAILABLE.getTitle();

    Assertions.assertAll(
            () -> Assertions.assertEquals(expectedCategory, actualResponse.getCategory(), "Expected category '%s', but got '%s'".formatted(expectedCategory, actualResponse.getCategory())),
            () -> Assertions.assertEquals(newDog.getName(), actualResponse.getName(), "Expected name '%s', but got '%s'".formatted(newDog.getName(), actualResponse.getName())),
            () -> Assertions.assertEquals(expectedStatus, actualResponse.getStatus(), "Expected status '%s', but got '%s'".formatted(expectedStatus, actualResponse.getStatus())),
            () -> Assertions.assertEquals(PHOTO_URL, actualResponse.getPhotoUrls(), "Expected photo url '%s', but got '%s'".formatted(PHOTO_URL, actualResponse.getPhotoUrls())),
            () -> Assertions.assertEquals(newDog.getId(), actualResponse.getId(), "Expected pet id '%s', but got '%s'".formatted(newDog.getId(), actualResponse.getId())),
            () -> Assertions.assertEquals(PET_TAG, actualResponse.getTags(), "Expected tag's '%s', but got '%s'".formatted(PET_TAG, actualResponse.getTags()))
    );

    PetResponseDTO actualGet = petsStoreService.petFindByPetId(newDog.getId())
            .statusCode(HttpStatus.SC_OK)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(PET_SCHEMA))
            .extract().body().as(PetResponseDTO.class);
    Assertions.assertAll(
            () -> Assertions.assertEquals(expectedCategory, actualGet.getCategory(), "GET: Expected category '%s', but got '%s'".formatted(expectedCategory, actualGet.getCategory())),
            () -> Assertions.assertEquals(newDog.getName(), actualGet.getName(), "GET: Expected name '%s', but got '%s'".formatted(expectedCategory, actualGet.getName())),
            () -> Assertions.assertEquals(expectedStatus, actualGet.getStatus(), "GET: Expected status '%s', but got '%s'".formatted(expectedCategory, actualGet.getStatus())),
            () -> Assertions.assertEquals(PHOTO_URL, actualGet.getPhotoUrls(), "GET: Expected photo url '%s', but got '%s'".formatted(expectedCategory, actualGet.getPhotoUrls())),
            () -> Assertions.assertEquals(newDog.getId(), actualGet.getId(), "GET: Expected pet id '%s', but got '%s'".formatted(expectedCategory, actualGet.getId())),
            () -> Assertions.assertEquals(PET_TAG, actualGet.getTags(), "GET: Expected tag's '%s', but got '%s'".formatted(expectedCategory, actualGet.getTags()))
    );
  }
}
