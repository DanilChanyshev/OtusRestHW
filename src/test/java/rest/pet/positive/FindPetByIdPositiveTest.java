package rest.pet.positive;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import petsstore.dto.PetDTO;
import petsstore.dto.PetResponseDTO;
import petsstore.model.Category;
import petsstore.model.StatusPet;
import petsstore.model.Tags;
import rest.pet.BasePetApiTest;
import wiremock.stubs.PetStubs;
import java.util.Collections;
import java.util.List;

public class FindPetByIdPositiveTest extends BasePetApiTest {

  private static final List<String> PHOTO_URLS = Collections.singletonList("svgPhoto1");

  @SneakyThrows
  @Test
  @Epic("Pet API")
  @Feature("Find pet")
  @Story("Find pet successfully with id")
  @DisplayName("Проверка поиска удаленного питомца по его id")
  void findDeletedPetByIdTest() {

    final PetDTO cat = PetDTO.builder()
            .id(RANDOM.nextLong())
            .category(Category.CAT.getCategory())
            .name("Marfa")
            .photoUrls(PHOTO_URLS)
            .tags(List.of(Tags.CAT_TAG.getTag()))
            .status(StatusPet.PENDING.getTitle())
            .build();

    PetStubs.stubCreatePet(wireMockServer, cat);
    PetStubs.stubGetPetById(wireMockServer, cat);

    petsStoreService.addNewPet(cat)
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(PetResponseDTO.class);

    petId = cat.getId();

    PetResponseDTO actualPet = petsStoreService.petFindByPetId(cat.getId())
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(PetResponseDTO.class);

    Assertions.assertAll(
            () -> Assertions.assertEquals(cat.getName(), actualPet.getName(), "GET: Expected name '%s', but got '%s'".formatted(cat.getName(), actualPet.getName())),
            () -> Assertions.assertEquals(cat.getStatus(), actualPet.getStatus(), "GET: Expected status '%s', but got '%s'".formatted(cat.getStatus(), actualPet.getStatus())),
            () -> Assertions.assertEquals(cat.getId(), actualPet.getId(), "GET: Expected pet id '%s', but got '%s'".formatted(cat.getId(), actualPet.getId()))
    );

    PetStubs.stubDeletePet(wireMockServer, petId);
    PetStubs.stubPetNotFound(wireMockServer, petId);

    petsStoreService.petDeleteByPetId(cat.getId())
            .statusCode(HttpStatus.SC_OK);

    petsStoreService.petFindByPetId(cat.getId())
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("type", Matchers.equalTo("error"))
            .body("message", Matchers.equalTo("Pet not found"));
  }
}
