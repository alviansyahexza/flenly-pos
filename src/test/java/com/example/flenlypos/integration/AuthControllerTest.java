package com.example.flenlypos.integration;

import com.example.flenlypos.ApiResponse;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.form.SignIn;
import com.example.flenlypos.auth.model.form.SignUp;
import com.example.flenlypos.auth.model.form.UserAdd;
import com.example.flenlypos.auth.repository.iface.StoreRepo;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.tools.security.UserRole;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    UserRepo userRepo;
    @MockitoBean
    StoreRepo storeRepo;
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
        Mockito.when(storeRepo.add(Mockito.anyString())).thenReturn(randomNum.nextInt(1,10));

        UserDto userDto = mockedUserDto();
        String password = userDto.getPassword();
        userDto.setPassword(passwordEncoder.encode(password));
        Mockito.when(userRepo.findByUsername(userDto.getUsername())).thenReturn(userDto);
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SignIn(password, userDto.getUsername())));
        mockMvc.perform(req).andExpect((res) -> bearerToken = res.getResponse().getContentAsString());
    }

    private UserDto mockedUserDto() {
        Instant now = Instant.now();
        return new UserDto(){{
            setId(randomNum.nextInt(1,10));
            setUsername(randomString.nextString());
            setPassword(randomString.nextString());
            setRole(UserRole.ROOT.val()); // for MVP user ROOT for testing
            setAuthorities(UserRole.ROOT.val()); // for MVP user ROOT for testing
            setCreatedOn(now);
            setLastUpdatedOn(now);
            setDeletedOn(now);
            setStoreId(randomNum.nextInt(1, 10));
        }};
    }

    @Test
    void signUp_success() throws Exception {
        UserDto userDto = mockedUserDto();
        SignUp signUp = new SignUp(userDto.getPassword(), userDto.getUsername());

        Mockito.when(storeRepo.add(Mockito.anyString())).thenReturn(userDto.getStoreId());
        Mockito.when(
                userRepo.signUp(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyInt(),
                        Mockito.any())
        ).thenReturn(userDto.getId());

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUp))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect((matcher) -> {
                    String contentAsString = matcher.getResponse().getContentAsString();
                    ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertTrue((boolean) apiRes.getData());
                });
    }

    @Test
    void signUp_invalid_body() throws Exception {
        UserDto userDto = mockedUserDto();
        SignUp signUp = new SignUp("1234", "12");

        Mockito.when(storeRepo.add(Mockito.anyString())).thenReturn(userDto.getStoreId());
        Mockito.when(
                userRepo.signUp(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyInt(),
                        Mockito.any())
        ).thenReturn(userDto.getId());

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .post("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUp))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req)
                .andExpect(status().isBadRequest())
                .andExpect((matcher) -> {
                    String contentAsString = matcher.getResponse().getContentAsString();
                    ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
                    Assertions.assertNull(apiRes.getData());
                    Assertions.assertTrue(apiRes.getError().contains("password: size must be between 5 and 100"));
                    Assertions.assertTrue(apiRes.getError().contains("username: size must be between 3 and 100"));
                });
    }

    @Test
    void add_success() throws Exception {
        UserDto userDto = mockedUserDto();
        UserAdd signUp = new UserAdd(userDto.getPassword(), userDto.getUsername(), UserRole.ADMIN.val());

        Mockito.when(storeRepo.add(Mockito.anyString())).thenReturn(userDto.getStoreId());
        Mockito.when(
                userRepo.signUp(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyInt(),
                        Mockito.any())
        ).thenReturn(userDto.getId());

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .post("/auth/add")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUp))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req).andExpect(status().isOk()).andExpect(result -> {
            String contentAsString = result.getResponse().getContentAsString();
            ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
            Assertions.assertTrue((boolean) apiRes.getData());
        });
    }

    @Test
    void add_invalid_body() throws Exception {
        UserDto userDto = mockedUserDto();
        UserAdd signUp = new UserAdd("1234", "12", UserRole.ADMIN.val());

        Mockito.when(storeRepo.add(Mockito.anyString())).thenReturn(userDto.getStoreId());
        Mockito.when(
                userRepo.signUp(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyInt(),
                        Mockito.any())
        ).thenReturn(userDto.getId());

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders
                .post("/auth/add")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUp))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(req).andExpect(status().isBadRequest()).andExpect(result -> {
            String contentAsString = result.getResponse().getContentAsString();
            ApiResponse apiRes = objectMapper.readValue(contentAsString, ApiResponse.class);
            Assertions.assertNull(apiRes.getData());
            Assertions.assertTrue(apiRes.getError().contains("password: size must be between 5 and 100"));
            Assertions.assertTrue(apiRes.getError().contains("username: size must be between 3 and 100"));
        });
    }
}
