package com.knu.ssingssing2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.knu.ssingssing2.api.ScooterController;
import com.knu.ssingssing2.model.scooter.Scooter;
import com.knu.ssingssing2.model.scooter.ScooterManufacture;
import com.knu.ssingssing2.payload.Location;
import com.knu.ssingssing2.payload.ScooterLocationRequest;
import com.knu.ssingssing2.repository.ScooterManufactureRepository;
import com.knu.ssingssing2.repository.ScooterRepository;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScooterControllerTest {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private MockMvc mockMvc;

  @Autowired
  private ScooterController scooterController;

  @Autowired
  private ScooterRepository scooterRepository;

  @Autowired
  private ScooterManufactureRepository scooterManufactureRepository;

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
    Location newLocation = new Location(36.123, 102.142);
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
      scooterRepository.save(new Scooter(scooterManufacture, "Nine Bot Series " + i, dummySerial(32)));
    }

    scooterRepository.save(new Scooter(scooterManufacture, "Test Bot", "ABCDEF"));
  }

  private String dummySerial(int length) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();

    String chars[] = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");

    for (int i=0 ; i < length; i++) {
      if (i != 0 && i % 8 == 0)
        sb.append("-");
      else
        sb.append(chars[random.nextInt(chars.length)]);

    }

    return sb.toString();
  }
}