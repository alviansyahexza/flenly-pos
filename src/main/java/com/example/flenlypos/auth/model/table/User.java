package com.example.flenlypos.auth.model.table;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Optional;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String authorities;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdOn;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdatedOn;
    @Column(insertable = false, updatable = false)
    private Instant deletedOn;

    private Integer storeId;
    private Integer addedById;

    public static User add(String username, String password, String role, int storeId, Optional<Integer> addedById) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setStoreId(storeId);
        addedById.ifPresent(user::setAddedById);
        return user;
    }
}
