package com.warehousing.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductStatus {
    WAITING("Ожидание"),
    DELIVERED("Доставлен"),
    ;

    private final String name;
}

