package petsstore.service;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import petsstore.dto.PetDTO;
import petsstore.model.StatusPet;

public class PetsStoreServiceImpl implements PetsStoreService{

  private static final String BASE_URL = System.getProperty("base.url");
  private static final String PET_ENDPOINT = "/pet";
  private static final String PET_FIND_BY_STATUS_ENDPOINT = "/pet/findByStatus";

  private final RequestSpecification baseSpec;

  public PetsStoreServiceImpl() {
    baseSpec = RestAssured.given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .log().all();
  }

  @Override
  @Step("Запись нового питомца")
  public ValidatableResponse addNewPet(final PetDTO petDTO) {
    return
            RestAssured.given(baseSpec)
                    .basePath(PET_ENDPOINT)
                    .body(petDTO)
                    .when()
                    .post()
                    .then()
                    .log().all();
  }

  @Override
  @Step("Поиск питомца по статусу")
  public ValidatableResponse petFindByStatus(final StatusPet statusPet) {
    return
            RestAssured.given(baseSpec)
                    .basePath(PET_FIND_BY_STATUS_ENDPOINT)
                    .queryParam("status", statusPet.getTitle())
                    .when()
                    .get()
                    .then()
                    .log().all();
  }

  @Override
  @Step("Удалить питомца по id")
  public ValidatableResponse petDeleteByPetId(final long petId) {
    return
            RestAssured.given(baseSpec)
                    .basePath(PET_ENDPOINT + "/" + petId)
                    .when()
                    .delete()
                    .then()
                    .log().all();
  }

  @Override
  @Step("Поиск питомца по id")
  public ValidatableResponse petFindByPetId(final long petId) {
    return
            RestAssured.given(baseSpec)
                    .basePath(PET_ENDPOINT + "/" + petId)
                    .when()
                    .get()
                    .then()
                    .log().all();
  }
}
