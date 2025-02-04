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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Integer storeId;

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
            setStoreId(user.getStoreId());
        }};
    }

    public Map<String, Object> toMap() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", this.id);
        claims.put("username", this.username);
        claims.put("password", this.password);
        claims.put("role", this.role);
        claims.put("authorities", this.authorities);
        claims.put("createdOn", this.createdOn.toEpochMilli());
        claims.put("lastUpdatedOn", this.lastUpdatedOn.toEpochMilli());
        claims.put("deletedOn", this.deletedOn.toEpochMilli());
        claims.put("storeId", this.storeId);
        return claims;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorities));
    }
}
