package PetsStore.service;

import PetsStore.Enum.StatusPet;
import PetsStore.dto.PetDTO;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PetsStoreApi {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String PET_ENDPOINT = "/pet";
    private static final String PET_FIND_BY_STATUS_ENDPOINT = "/pet/findByStatus";

    private final RequestSpecification base_spec;

    public PetsStoreApi() {
        base_spec = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .log().all();
    }

    public ValidatableResponse addNewPet(final PetDTO petDTO) {
        return
                given(base_spec)
                        .basePath(PET_ENDPOINT)
                        .body(petDTO)
                        .when()
                        .post()
                        .then()
                        .log().all();
    }

    public ValidatableResponse petFindByStatus(final StatusPet statusPet) {
        return
                given(base_spec)
                        .basePath(PET_FIND_BY_STATUS_ENDPOINT)
                        .queryParam(statusPet.getTitle())
                        .when()
                        .get()
                        .then()
                        .log().all();
    }

    public ValidatableResponse petDeleteByPetId(final long petId) {
        return
                given(base_spec)
                        .basePath(PET_ENDPOINT + "/" + petId)
                        .when()
                        .delete()
                        .then()
                        .log().all();
    }

    public ValidatableResponse petFindByPetId(final long petId) {
        return
                given(base_spec)
                        .basePath(PET_ENDPOINT + "/" + petId)
                        .when()
                        .get()
                        .then()
                        .log().all();
    }
}
