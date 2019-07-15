package com.library.model.data.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {

    private long id;

    private String name;
}
