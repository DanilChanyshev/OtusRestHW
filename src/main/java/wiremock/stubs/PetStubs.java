package wiremock.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import jsonutils.JsonUtils;
import petsstore.dto.PetDTO;
import petsstore.model.StatusPet;
import java.util.List;

public class PetStubs {

  private static final String URL_PET = "/pet";
  private static final String URL_FIND_STATUS = "/findByStatus";

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Step("Стаб создания питомца без обязательного поля 'status'")
  public static void stubCreatePetInvalid(WireMockServer wm) {
    String body = JsonUtils.loadJson("fixtures/invalideInput.json");
    wm.stubFor(post(urlEqualTo(URL_PET))
            .withRequestBody(notMatching(".*\"status\".*"))
            .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody(body)));
  }

  @Step("Стаб негативного поиска питомца в базе")
  public static void stubPetNotFound(WireMockServer wm, long petId) {
    String body = JsonUtils.loadJson("fixtures/petNotFound.json");
    wm.stubFor(get(urlEqualTo(URL_PET.concat("/") + petId))
            .willReturn(aResponse()
                    .withStatus(404)
                    .withHeader("Content-Type", "application/json")
                    .withBody(body)));
  }

  @Step("Стаб создания питомца")
  public static void stubCreatePet(WireMockServer wm, PetDTO pet) throws Exception {
    String body = MAPPER.writeValueAsString(pet);

    wm.stubFor(post(urlEqualTo(URL_PET))
            .willReturn(okJson(body)));
  }

  @Step("Стаб поиска потомца по Id")
  public static void stubGetPetById(WireMockServer wm, PetDTO pet) throws Exception {
    String body = MAPPER.writeValueAsString(pet);

    wm.stubFor(get(urlEqualTo(URL_PET.concat("/") + pet.getId()))
            .willReturn(okJson(body)));
  }

  @Step("Стаб удаления питомца")
  public static void stubDeletePet(WireMockServer wm, long petId) {
    wm.stubFor(delete(urlEqualTo(URL_PET.concat("/") + petId))
            .willReturn(aResponse().withStatus(200)));
  }

  @Step("Стаб поиска питомца по статусу")
  public static void stubFindByStatus(WireMockServer wm, StatusPet status, List<PetDTO> pets) throws Exception {
    String body = MAPPER.writeValueAsString(pets);

    wm.stubFor(get(urlPathEqualTo(URL_PET.concat(URL_FIND_STATUS)))
            .withQueryParam("status", equalTo(status.getTitle()))
            .willReturn(okJson(body)));
  }
}