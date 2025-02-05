package com.example.flenlypos.repo;

import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.table.Store;
import com.example.flenlypos.auth.model.table.User;
import com.example.flenlypos.auth.repository.iface.StoreRepo;
import com.example.flenlypos.auth.repository.iface.StoreRepoJpa;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.repository.iface.UserRepoJpa;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
public class UserTest {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserRepoJpa userRepoJpa;
    @Autowired
    StoreRepo storeRepo;
    @Autowired
    StoreRepoJpa storeRepoJpa;
    RandomString randomString = new RandomString(12);

    @Test
    void signUp() {
        String storeName = randomString.nextString();

        int storeId = storeRepo.add(storeName);
        Optional<Store> optionalStore = storeRepoJpa.findById(storeId);
        Assertions.assertTrue(optionalStore.isPresent());

        Store store = optionalStore.get();
        Assertions.assertEquals(store.getName(), storeName);
        Assertions.assertTrue(
                store.getCreatedOn().isBefore(Instant.now())
                        && store.getCreatedOn().isAfter(Instant.now().minusSeconds(3))
                        && store.getLastUpdatedOn().isBefore(Instant.now())
                        && store.getLastUpdatedOn().isAfter(Instant.now().minusSeconds(3))
                        && store.getDeletedOn() == null
        );

        String username = randomString.nextString();
        String password = randomString.nextString();
        String role = randomString.nextString();
        int userId = userRepo.signUp(username, password, role, storeId, Optional.empty());
        Optional<User> optionalUser = userRepoJpa.findById(userId);
        Assertions.assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();
        Assertions.assertTrue(
                user.getUsername().equals(username)
                        && user.getRole().equals(role)
                        && user.getPassword().equals(password)
        );
        Assertions.assertTrue(
                user.getCreatedOn().isBefore(Instant.now())
                        && user.getCreatedOn().isAfter(Instant.now().minusSeconds(3))
                        && user.getLastUpdatedOn().isBefore(Instant.now())
                        && user.getLastUpdatedOn().isAfter(Instant.now().minusSeconds(3))
                        && user.getDeletedOn() == null
        );
    }
//    UserDto findByUsername(String username);
//    int signUp(String username, String password, String role, int storeId, Optional<Integer> addedById);
}
