package com.library.model.data.entity;

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

    private long roleId;

    private int karma;
}
