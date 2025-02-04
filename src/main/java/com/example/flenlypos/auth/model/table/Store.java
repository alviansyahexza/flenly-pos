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
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdOn;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdatedOn;
    @Column(insertable = false, updatable = false)
    private Instant deletedOn;
}
