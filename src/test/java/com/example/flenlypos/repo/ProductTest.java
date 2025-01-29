package com.example.flenlypos.repo;

import com.example.flenlypos.inventory.model.dto.ProductDto;
import com.example.flenlypos.inventory.repository.impl.ProductRepoImpl;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Random;

/**
 * NOT A BEST PRACTICE, JUST FOR MVP
 * Make sure to change application.properties
 * spring.jpa.hibernate.ddl-auto to create-drop
 * on literally testing database
 * */
@SpringBootTest()
public class ProductTest {

    @Autowired
    ProductRepoImpl productRepo;
    RandomString randomString = new RandomString(12);
    Random randomNum = new Random(5);

    ProductDto productDto() {
        String name = randomString.nextString();
        int stock = randomNum.nextInt();
        long price = randomNum.nextLong();
        String qrCode = randomString.nextString();

        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setStock(stock);
        productDto.setQrCode(qrCode);

        return productDto;
    }

    @Test
    void saveProduct() {
        ProductDto init = productDto();
        ProductDto productDto = init.toBuilder().build();
        Integer id = productRepo.add(productDto);
        productDto = productRepo.findById(id);

        //test new
        Assertions.assertNull(productDto.getDeletedOn());
        Assertions.assertNotNull(productDto.getId());
        Assertions.assertNotNull(productDto.getCreatedOn());
        Assertions.assertNotNull(productDto.getLastUpdatedOn());
        Assertions.assertEquals(productDto.getName(), init.getName());
        Assertions.assertEquals(productDto.getPrice(), init.getPrice());
        Assertions.assertEquals(productDto.getStock(), init.getStock());
        Assertions.assertEquals(productDto.getQrCode(), init.getQrCode());

        //test update
        String name = productDto.getName() + randomString.nextString();
        int stock = productDto.getStock() + randomNum.nextInt();
        long price = productDto.getPrice() + randomNum.nextLong();
        String qrCode = productDto.getQrCode() + randomString.nextString();
        Instant prevLastUpdate = productDto.getLastUpdatedOn();
        Integer prevId = productDto.getId();

        productDto.setName(name);
        productDto.setStock(stock);
        productDto.setPrice(price);
        productDto.setQrCode(qrCode);
        productRepo.update(productDto);
        productDto = productRepo.findById(id);

        Assertions.assertNull(productDto.getDeletedOn());
        Assertions.assertTrue(productDto.getLastUpdatedOn().isAfter(prevLastUpdate));

        Assertions.assertNotEquals(productDto.getName(), init.getName());
        Assertions.assertNotEquals(productDto.getPrice(), init.getPrice());
        Assertions.assertNotEquals(productDto.getStock(), init.getStock());
        Assertions.assertNotEquals(productDto.getQrCode(), init.getQrCode());

        Assertions.assertEquals(productDto.getId(), prevId);
        Assertions.assertEquals(productDto.getName(), name);
        Assertions.assertEquals(productDto.getPrice(), price);
        Assertions.assertEquals(productDto.getStock(), stock);
        Assertions.assertEquals(productDto.getQrCode(), qrCode);

        //test delete
        productRepo.delete(id);
        productDto = productRepo.findById(id);

        Assertions.assertTrue(productDto.getDeletedOn().isBefore(Instant.now()));

        //test find all no deleted
        Iterable<ProductDto> all = productRepo.findAll(1, 1);
        Assertions.assertTrue( !all.iterator().hasNext() || all.iterator().next().getDeletedOn() == null);

        //test find all with deleted
        all = productRepo.findAll(1, 1, true);
        Assertions.assertTrue(all.iterator().hasNext() && all.iterator().next().getDeletedOn().isBefore(Instant.now()));
    }
}
