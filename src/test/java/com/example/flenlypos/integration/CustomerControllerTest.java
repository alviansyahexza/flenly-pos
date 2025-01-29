package com.example.flenlypos.integration;

import com.example.flenlypos.ApiResponse;
import com.example.flenlypos.customer.model.dto.CustomerDto;
import com.example.flenlypos.customer.model.form.CustomerForm;
import com.example.flenlypos.customer.repository.impl.CustomerRepoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    CustomerRepoImpl customerRepo;
    private final RandomString randomString = new RandomString(12);
    private final Random randomNum = new Random();

    @BeforeEach
    void init() {
        noCallRepo();
    }

    private void noCallRepo() {
        CustomerDto customerDto = mockedCustomerDto();
        Mockito.doNothing().when(customerRepo).delete(Mockito.anyInt());
        Mockito.when(customerRepo.add(Mockito.any(CustomerDto.class))).thenReturn(1);
        Mockito.when(customerRepo.findById(Mockito.anyInt())).thenReturn(customerDto);
    }

    private CustomerDto mockedCustomerDto() {
        Instant now = Instant.now();
        return CustomerDto.builder()
                .id(randomNum.nextInt(1, 10))
                .name(randomString.nextString())
                .email(randomString.nextString())
                .phone(randomString.nextString())
                .createdOn(now)
                .lastUpdatedOn(now) // on create, lastUpdate = onCreate
                .build();
    }

    @Test
    void noBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_success() throws Exception {
        CustomerDto mockedCustomerDto = mockedCustomerDto();
        Mockito.when(customerRepo.add(Mockito.any(CustomerDto.class))).thenReturn(mockedCustomerDto.getId());
        Mockito.when(customerRepo.findById(mockedCustomerDto.getId())).thenReturn(mockedCustomerDto);
        CustomerForm form = new CustomerForm(
                mockedCustomerDto.getName(),
                mockedCustomerDto.getEmail(),
                mockedCustomerDto.getPhone()
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((matcher) -> {
                    String contentAsString = matcher.getResponse().getContentAsString();
                    ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
                    CustomerDto res = objectMapper.convertValue(apiRes.getData(), CustomerDto.class);
                    Assertions.assertNull(apiRes.getError());
                    Assertions.assertEquals(res, mockedCustomerDto);
                });
    }

    @Test
    void add_invalidBody() throws Exception {
        CustomerForm form = new CustomerForm("", "", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNull(apiResponse.getData());
                    Assertions.assertEquals("either email or phone should be filled", apiResponse.getError());
                });
    }

    @Test
    void delete_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/" + Mockito.anyInt()))
                .andExpect(status().isOk())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertTrue((boolean) apiResponse.getData());
                    Assertions.assertNull(apiResponse.getError());
                });
    }

    @Test
    void find_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + Mockito.anyInt()))
                .andExpect(status().isOk())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNotNull(apiResponse.getData());
                    Assertions.assertNull(apiResponse.getError());
                });
    }
}
