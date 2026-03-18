package rest.pet.negative;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rest.pet.BasePetApiTest;
import wiremock.stubs.PetStubs;

public class FindPetByIdNegativeTest extends BasePetApiTest {

  @Test
  @Epic("Pet API Negative")
  @Feature("Find a pet")
  @Story("Find a pet failed")
  @DisplayName("Проверка поиска питомца по несуществующему id")
  void findPetByNotValidId() {

    final long FAKE_ID = Long.MAX_VALUE - RANDOM.nextLong(1000);

    PetStubs.stubPetNotFound(wireMockServer, FAKE_ID);

    petsStoreService.petFindByPetId(FAKE_ID)
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("type", Matchers.equalTo("error"))
            .body("message", Matchers.equalTo("Pet not found"));
  }
}
