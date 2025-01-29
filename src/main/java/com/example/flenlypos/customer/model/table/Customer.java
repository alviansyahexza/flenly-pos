package com.example.flenlypos.customer.model.table;

import com.example.flenlypos.customer.model.dto.CustomerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE customer SET deleted_on = CURRENT_TIMESTAMP WHERE id=?")
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String phone;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdatedOn;

    @Column(insertable = false, updatable = false)
    private Instant deletedOn;

    public static void fromDto(CustomerDto customerDto, Customer customer) {
        customer.id = customerDto.getId();
        customer.name = customerDto.getName();
        customer.email = customerDto.getEmail();
        customer.phone = customerDto.getPhone();
    }
}
