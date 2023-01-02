package com.home_manager.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddHomesGroupDTO {

    @NotEmpty(message = "{not_empty}")
    @Size(min = 2, max = 20, message = "{field_length}")
    private String name;

    private int size;

    @NotEmpty(message = "{not_empty}")
    @NotNull(message = "{not_empty}")
    private String type;

    public AddHomesGroupDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
