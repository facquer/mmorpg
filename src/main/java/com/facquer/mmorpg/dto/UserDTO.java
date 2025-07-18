package com.facquer.mmorpg.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
}
