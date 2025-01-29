package com.example.flenlypos.auth.model.dto;

import com.example.flenlypos.auth.model.table.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String authorities;
    private Instant createdOn;
    private Instant lastUpdatedOn;
    private Instant deletedOn;

    public static UserDto fromUser(User user) {
        return new UserDto(){{
            setId(user.getId());
            setUsername(user.getUsername());
            setPassword(user.getPassword());
            setRole(user.getRole());
            setAuthorities(user.getAuthorities());
            setCreatedOn(user.getCreatedOn());
            setLastUpdatedOn(user.getLastUpdatedOn());
            setDeletedOn(user.getDeletedOn());
        }};
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorities));
    }
}
