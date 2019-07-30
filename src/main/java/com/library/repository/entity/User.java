package com.library.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private long id;

    private String email;

    private String password;

    private String phone;

    private String firstName;

    private String lastName;

    private Role role;

    private int karma;
}
