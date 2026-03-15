package petsstore.service;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import petsstore.dto.PetDTO;
import petsstore.model.StatusPet;

public interface PetsStoreService {

  @Step("Запись нового питомца")
  ValidatableResponse addNewPet(final PetDTO petDTO);

  @Step("Поиск питомца по статусу")
  ValidatableResponse petFindByStatus(final StatusPet statusPet);

  @Step("Удалить питомца по id")
  ValidatableResponse petDeleteByPetId(final long petId);

  @Step("Поиск питомца по id")
  ValidatableResponse petFindByPetId(final long petId);
}
