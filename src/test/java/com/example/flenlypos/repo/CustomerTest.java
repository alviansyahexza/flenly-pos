package com.example.flenlypos.repo;

import com.example.flenlypos.customer.model.dto.CustomerDto;
import com.example.flenlypos.customer.repository.impl.CustomerRepoImpl;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

/**
 * NOT A BEST PRACTICE, JUST FOR MVP
 * Make sure to change application.properties
 * spring.jpa.hibernate.ddl-auto to create-drop
 * on literally testing database
 * */
@SpringBootTest()
public class CustomerTest {

    @Autowired
    CustomerRepoImpl customerRepo;
    RandomString randomString = new RandomString(12);

    CustomerDto customerDto() {
        return CustomerDto.builder()
                .name(randomString.nextString())
                .email(randomString.nextString())
                .phone(randomString.nextString())
                .build();
    }

    @Test
    void saveCustomer() {
        CustomerDto init = customerDto();
        CustomerDto customerDto = init.toBuilder().build();
        Integer id = customerRepo.add(customerDto);
        customerDto = customerRepo.findById(id);

        //test new
        Assertions.assertNull(customerDto.getDeletedOn());
        Assertions.assertNotNull(customerDto.getId());
        Assertions.assertNotNull(customerDto.getCreatedOn());
        Assertions.assertNotNull(customerDto.getLastUpdatedOn());
        Assertions.assertEquals(customerDto.getName(), init.getName());
        Assertions.assertEquals(customerDto.getEmail(), init.getEmail());
        Assertions.assertEquals(customerDto.getPhone(), init.getPhone());

        //test delete
        customerRepo.delete(id);
        customerDto = customerRepo.findById(id);
        Assertions.assertTrue(customerDto.getDeletedOn().isBefore(Instant.now()));
    }
}
