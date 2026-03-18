package rest.pet.positive;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import petsstore.dto.PetDTO;
import petsstore.dto.PetResponseDTO;
import petsstore.dto.TagDTO;
import petsstore.model.Category;
import petsstore.model.StatusPet;
import petsstore.model.Tags;
import rest.pet.BasePetApiTest;
import wiremock.stubs.PetStubs;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindPetByStatusTest extends BasePetApiTest {

  private static final List<String> PHOTO_URL = Collections.singletonList("svgPhoto1");
  private static final List<TagDTO> TAGS = List.of(Tags.DOG_TAG.getTag(), Tags.COLOR_WHITE_TAG.getTag());
  private static final String FIND_PET_SCHEMA = "schema/CreatePetSchema.json";
  private static final StatusPet STATUS = StatusPet.SOLD;

  @SneakyThrows
  @Test
  @Epic("Pet API")
  @Feature("Find pet")
  @Story("Find pet successfully with status")
  @DisplayName("RestApi. Проверка поиска питомца по статусу. Проверка заполненности полей")
  void petFindByStatus() {
    final PetDTO newDog = PetDTO.builder()
            .id(RANDOM.nextLong())
            .category(Category.DOG.getCategory())
            .name("Marc")
            .photoUrls(PHOTO_URL)
            .tags(TAGS)
            .status(STATUS.getTitle())
            .build();

    petId = newDog.getId();

    PetStubs.stubCreatePet(wireMockServer, newDog);
    PetStubs.stubFindByStatus(wireMockServer, STATUS, List.of(newDog));

    petsStoreService.addNewPet(newDog)
            .statusCode(HttpStatus.SC_OK)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(FIND_PET_SCHEMA));

    List<PetResponseDTO> petList = Arrays.stream(petsStoreService.petFindByStatus(STATUS)
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .as(PetResponseDTO[].class))
            .toList();

    petList.forEach(pet -> Assertions.assertEquals(STATUS.getTitle(), pet.getStatus(),
            "Expected status = %s, actual status = %s".formatted(STATUS.getTitle(), pet.getStatus())));

    List<PetResponseDTO> correct = petList.stream()
            .filter(pet -> newDog.getId().equals(pet.getId()))
            .toList();

    if (correct.size() != 1) {
      throw new RuntimeException(
              "Expected to find 1 pet with ID: %s \n Found: %s".formatted(newDog.getId(), correct.size()));
    }
  }

  @SneakyThrows
  @Test
  @Epic("Pet API")
  @Feature("Find pet")
  @Story("Find pet successfully with status and check timezone")
  @DisplayName("RestApi. Проверка поиска питомца по статусу. Проверка времени отработки")
  void checkTimeLimit() {
    final PetDTO newDog = PetDTO.builder()
            .id(RANDOM.nextLong())
            .category(Category.DOG.getCategory())
            .name("Hasky")
            .photoUrls(PHOTO_URL)
            .tags(TAGS)
            .status(StatusPet.SOLD.getTitle())
            .build();

    petId = newDog.getId();

    PetStubs.stubCreatePet(wireMockServer, newDog);
    PetStubs.stubFindByStatus(wireMockServer, StatusPet.SOLD, List.of(newDog));

    petsStoreService.addNewPet(newDog)
            .statusCode(HttpStatus.SC_OK);

    petsStoreService.petFindByStatus(StatusPet.SOLD)
            .statusCode(HttpStatus.SC_OK)
            .time(Matchers.lessThan(1_500L));
  }
}
