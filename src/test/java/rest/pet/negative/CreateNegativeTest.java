package rest.pet.negative;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import petsstore.dto.PetDTO;
import petsstore.dto.TagDTO;
import petsstore.model.Category;
import petsstore.model.Tags;
import rest.pet.BasePetApiTest;
import wiremock.stubs.PetStubs;

import java.util.List;

public class CreateNegativeTest extends BasePetApiTest {

  private static final List<TagDTO> PET_TAG = List.of(
          Tags.DOG_TAG.getTag(),
          Tags.COLOR_WHITE_TAG.getTag());

  @Test
  @Epic("Pet API Negative")
  @Feature("Create pet")
  @Story("Create pet")
  @DisplayName("RespApi. Проверка создания питомца без обязательных полей.")
  void createPetWithoutRequiredFieldTest() {

    PetDTO invalidPet = PetDTO.builder()
            .name("Hasky")
            .id(Math.abs(RANDOM.nextLong()))
            .category(Category.DOG.getCategory())
            .tags(PET_TAG)
            .build();

    PetStubs.stubCreatePetInvalid(wireMockServer);
    PetStubs.stubPetNotFound(wireMockServer, invalidPet.getId());

    petsStoreService.addNewPet(invalidPet)
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("message", Matchers.containsString("Invalid input"));

    petsStoreService.petFindByPetId(invalidPet.getId())
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("type", Matchers.equalTo("error"))
            .body("message", Matchers.equalTo("Pet not found"));
  }
}
