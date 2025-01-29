package com.example.flenlypos.auth.model.table;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

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
}
