package com.example.flenlypos.integration;

import com.example.flenlypos.ApiResponse;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.form.SignIn;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.repository.impl.UserRepoImpl;
import com.example.flenlypos.auth.service.UserService;
import com.example.flenlypos.auth.tools.security.UserRole;
import com.example.flenlypos.inventory.model.dto.ProductDto;
import com.example.flenlypos.inventory.model.form.ProductForm;
import com.example.flenlypos.inventory.repository.iface.ProductRepo;
import com.example.flenlypos.inventory.repository.impl.ProductRepoImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    ProductRepo productRepoIface;
    @MockitoBean
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final RandomString randomString = new RandomString(12);
    private final Random randomNum = new Random();
    private String bearerToken;

    @BeforeEach
    void init() throws Exception {
        noCallRepo();
    }

    private void noCallRepo() throws Exception {
        ProductDto productDto = mockedProductDto();
        Mockito.doNothing().when(productRepoIface).delete(Mockito.anyInt());
        Mockito.doNothing().when(productRepoIface).update(Mockito.any());
        Mockito.when(productRepoIface.add(Mockito.any(ProductDto.class))).thenReturn(1);
        Mockito.when(productRepoIface.findById(Mockito.anyInt())).thenReturn(productDto);
        Mockito.when(productRepoIface.findAll()).thenReturn(new ArrayList<>(){{add(productDto);}});
        Mockito.when(productRepoIface.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>(){{add(productDto);}});
        Mockito.when(productRepoIface.findAll(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(new ArrayList<>(){{add(productDto);}});

        UserDto userDto = mockedUserDto();
        String password = userDto.getPassword();
        userDto.setPassword(passwordEncoder.encode(password));
        Mockito.when(userRepo.findByUsername(userDto.getUsername())).thenReturn(userDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignIn(password, userDto.getUsername())))
                )
                .andExpect((res) -> {
                    bearerToken = res.getResponse().getContentAsString();
                });
    }

    private ProductDto mockedProductDto() {
        int mockedProductId = randomNum.nextInt(1,100);
        String mockedProductName = randomString.nextString();
        long mockedProductPrice = randomNum.nextLong(1,100);
        int mockedProductStock = randomNum.nextInt(1,100);
        String mockedProductQrCode = randomString.nextString();
        Instant mockedProductCreatedOn = Instant.now();

        return ProductDto.builder()
                .id(mockedProductId)
                .name(mockedProductName)
                .price(mockedProductPrice)
                .stock(mockedProductStock)
                .qrCode(mockedProductQrCode)
                .createdOn(mockedProductCreatedOn)
                .lastUpdatedOn(mockedProductCreatedOn) // on create, lastUpdate = onCreate
                .build();
    }

    private UserDto mockedUserDto() {
        Instant now = Instant.now();
        UserDto userDto = new UserDto(){{
            setId(randomNum.nextInt(1,10));
            setUsername(randomString.nextString());
            setPassword(randomString.nextString());
            setRole(UserRole.ROOT.val()); // for MVP user ROOT for testing
            setAuthorities(UserRole.ROOT.val()); // for MVP user ROOT for testing
            setCreatedOn(now);
            setLastUpdatedOn(now);
            setDeletedOn(now);
        }};
        return userDto;
    }

    @Test
    void noBody() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req).andExpect(status().isBadRequest());
    }

    @Test
    void add_success() throws Exception {
        ProductDto mockedProductDto = mockedProductDto();
        Mockito.when(productRepoIface.add(Mockito.any(ProductDto.class))).thenReturn(mockedProductDto.getId());
        Mockito.when(productRepoIface.findById(mockedProductDto.getId())).thenReturn(mockedProductDto);
        ProductForm form = new ProductForm(
                mockedProductDto.getName(),
                mockedProductDto.getPrice(),
                mockedProductDto.getStock(),
                mockedProductDto.getQrCode()
        );
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect((matcher) -> {
                    String contentAsString = matcher.getResponse().getContentAsString();
                    ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
                    ProductDto res = objectMapper.convertValue(apiRes.getData(), ProductDto.class);
                    Assertions.assertNull(apiRes.getError());
                    Assertions.assertEquals(res, mockedProductDto);
                });
    }

    @Test
    void add_invalidBody() throws Exception {
        ProductForm form = new ProductForm("", 0L, 0, "");
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNull(apiResponse.getData());
                    Assertions.assertTrue(apiResponse.getError().contains("name: must not be blank"));
                    Assertions.assertTrue(apiResponse.getError().contains("price: must be greater than or equal to 1"));
                    Assertions.assertTrue(apiResponse.getError().contains("stock: must be greater than or equal to 1"));
                });
    }

    @Test
    void update_success() throws Exception {
        ProductDto mockedProductDto = mockedProductDto();
        Mockito.when(productRepoIface.add(Mockito.any(ProductDto.class))).thenReturn(mockedProductDto.getId());
        Mockito.when(productRepoIface.findById(mockedProductDto.getId())).thenReturn(mockedProductDto);
        ProductForm form = new ProductForm(
                mockedProductDto.getName(),
                mockedProductDto.getPrice(),
                mockedProductDto.getStock(),
                mockedProductDto.getQrCode()
        );
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.put("/products/" + mockedProductDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect((matcher) -> {
                    String contentAsString = matcher.getResponse().getContentAsString();
                    ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
                    ProductDto res = objectMapper.convertValue(apiRes.getData(), ProductDto.class);
                    Assertions.assertNull(apiRes.getError());
                    Assertions.assertEquals(res, mockedProductDto);
                });
    }

    @Test
    void update_invalidBody() throws Exception {
        ProductForm form = new ProductForm("", 0L, 0, "");
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.put("/products/" + -1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNull(apiResponse.getData());
                    Assertions.assertTrue(apiResponse.getError().contains("name: must not be blank"));
                    Assertions.assertTrue(apiResponse.getError().contains("price: must be greater than or equal to 1"));
                    Assertions.assertTrue(apiResponse.getError().contains("stock: must be greater than or equal to 1"));
                });
    }

    @Test
    void findAll_success() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/products")
                .param("page", "1")
                .param("size", "2")
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    List list = objectMapper.convertValue(apiResponse.getData(), List.class);
                    Assertions.assertNull(apiResponse.getError());
                    Assertions.assertTrue(list.size() <= 2);
                });
    }

    @Test
    void findAll_invalidParams() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/products")
                .param("page", "0")
                .param("size", "0")
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNull(apiResponse.getData());
                    Assertions.assertTrue(apiResponse.getError().contains("page: must be greater than or equal to 1"));
                    Assertions.assertTrue(apiResponse.getError().contains("size: must be greater than or equal to 1"));
                });
    }

    @Test
    void delete_success() throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .delete("/products/" + 1)
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
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
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .get("/products/" + Mockito.anyInt())
                .header("Authorization", "Bearer " + bearerToken);
        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andDo((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNotNull(apiResponse.getData());
                    Assertions.assertNull(apiResponse.getError());
                });
    }
}
