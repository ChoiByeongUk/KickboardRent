package com.knu.ssingssing2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.knu.ssingssing2.api.ScooterController;
import com.knu.ssingssing2.model.Location;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.payload.ScooterLocationRequest;
import com.knu.ssingssing2.model.scooter.ScooterState;
import com.knu.ssingssing2.repository.ScooterManufactureRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.knu.ssingssing2.Utils.dummySerial;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScooterControllerTest {

  @Autowired
  private ScooterController scooterController;

  @Autowired
  private ScooterRepository scooterRepository;

  @Autowired
  private ScooterManufactureRepository scooterManufactureRepository;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private MockMvc mockMvc;

  @Before
  public void init() {
    mockMvc = standaloneSetup(scooterController).build();
    createDummyData();
  }

  @Test
  public void getAllScootersWithoutPaging() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/scooters"))
        .andExpect(status().isOk())
        .andReturn();

    logger.info(result.getResponse().getContentAsString());
  }

  @Test
  public void updateScooterLocation() throws Exception {
    Location newLocation = new Location(36.123, 102.142, "경북대 정문");
    ScooterLocationRequest request = new ScooterLocationRequest("ABCDEF", newLocation);

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = objectWriter.writeValueAsString(request);

    mockMvc.perform(put("/api/scooters/location")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(requestJson))
        .andExpect(status().isOk());
  }

  private void createDummyData() {
    ScooterManufacture scooterManufacture;

    scooterManufacture = new ScooterManufacture("Xiomi");
    scooterManufactureRepository.save(scooterManufacture);

    for (int i = 1; i <= 100; i++) {
      scooterRepository.save(
          Scooter.builder()
              .manufacture(scooterManufacture)
              .modelName("Nine Bot Series " + i)
              .serial(dummySerial(32))
              .state(ScooterState.AVAILABLE)
              .location(new Location(200, 200, "경북대 쪽문"))
              .build()
      );
    }
  }

}
