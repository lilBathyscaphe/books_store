package com.epam.bookstore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "bookstore_role")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "role_id")
    @NotNull
    private Integer id;

    @Column(name = "role_name")
    @NotNull
    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Override
    public String getAuthority() {
        return getName();
    }

}
