package com.epam.bookstore.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserRepresentation {

    private String username;
    private String firstname;
    private String lastname;
    private Set<Role> roles;

    public UserRepresentation(User user) {
        if (user != null) {
            this.username = user.getUsername();
            this.firstname = user.getFirstname();
            this.lastname = user.getLastname();
            this.roles = user.getRoles();
        }
    }
}
