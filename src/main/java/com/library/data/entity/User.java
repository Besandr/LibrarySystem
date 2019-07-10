package com.library.data.entity;

import lombok.Data;

@Data
public class User {

    private long id;

    private String email;

    private String password;

    private String phone;

    private String firstName;

    private String lastName;

    private long roleId;

    private int karma;
}
