package rest.pet;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import petsstore.service.PetsStoreService;
import petsstore.service.PetsStoreServiceImpl;
import java.util.Random;

public class BasePetApiTest {
  protected static final Random RANDOM = new Random(12345L);
  protected static WireMockServer wireMockServer;
  protected PetsStoreService petsStoreService;
  protected Long petId;


  @BeforeAll
  static void setup() {
    wireMockServer = new WireMockServer(8089);
    wireMockServer.start();

    System.setProperty("base.url", "http://localhost:8089");
  }

  @BeforeEach
  void init() {
    petsStoreService = new PetsStoreServiceImpl();
    wireMockServer.resetAll();
  }

  @AfterAll
  static void tearDown() {
    wireMockServer.stop();
  }
}
