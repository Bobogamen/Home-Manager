package com.home_manager.model.enums;

public enum HomesGroupEnum {

    APARTMENT_BUILDING("Жилищен блок");
//    HOUSE_COMPLEX("Комплекс къщи");

    private final String name;

    HomesGroupEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
