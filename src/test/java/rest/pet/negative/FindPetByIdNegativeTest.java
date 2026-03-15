package rest.pet.negative;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rest.pet.BasePetApiTest;

public class FindPetByIdNegativeTest extends BasePetApiTest {

  @Test
  @Epic("Pet API Negative")
  @Feature("Find a pet")
  @Story("Find a pet failed")
  @DisplayName("Проверка поиска питомца по несуществующему id")
  void findPetByNotValidId() {
    petsStoreService.petFindByPetId(Long.MAX_VALUE - RANDOM.nextLong(1000))
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("type", Matchers.equalTo("error"))
            .body("message", Matchers.equalTo("Pet not found"));
  }
}
