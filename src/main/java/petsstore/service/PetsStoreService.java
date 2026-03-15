package petsstore.service;

import io.restassured.response.ValidatableResponse;
import petsstore.dto.PetDTO;
import petsstore.model.StatusPet;

public interface PetsStoreService {
  ValidatableResponse addNewPet(final PetDTO petDTO);
  ValidatableResponse petFindByStatus(final StatusPet statusPet);
  ValidatableResponse petDeleteByPetId(final long petId);
  ValidatableResponse petFindByPetId(final long petId);
}
