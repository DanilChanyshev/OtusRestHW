package rest.pet;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;
import petsstore.service.PetsStoreService;
import petsstore.service.PetsStoreServiceImpl;
import java.util.Random;

public class BasePetApiTest {
  protected static final Random RANDOM = new Random(12345L);
  protected PetsStoreService petsStoreService;
  protected Long petId;

  @BeforeEach
  void setUp() {
    petsStoreService = new PetsStoreServiceImpl();
    ((Logger) LoggerFactory.getLogger("org.apache.http")).setLevel(Level.INFO);
    ((Logger) LoggerFactory.getLogger("io.restassured")).setLevel(Level.INFO);
    petId = null;
  }

  @AfterEach
  void tearDown() {
    if (petId != null) {
      Response response = petsStoreService.petDeleteByPetId(petId).extract().response();
      if (response.statusCode() != HttpStatus.SC_OK && response.statusCode() != HttpStatus.SC_NOT_FOUND){
        Assertions.fail("Pet removal failed");
      }
    }
  }
}
