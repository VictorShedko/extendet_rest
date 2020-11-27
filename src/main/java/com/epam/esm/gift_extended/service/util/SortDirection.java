package com.epam.esm.gift_extended.service.util;

import com.epam.esm.gift_extended.exception.JSONExceptionEntity;

public enum SortDirection {
    ASC("ASC"),
    DESC("DESC");

    String type;

    SortDirection(String type) {
        this.type=type;
    }

    public String getTypeAsString() {
        return type;
    }

    public static SortDirection getByStringOrDefault(String requestedDirection){
        for (var sortDirection : SortDirection.values()) {
            if (sortDirection.getTypeAsString().equalsIgnoreCase(requestedDirection) ) {
                return sortDirection;
            }
        }
        return ASC;
    }
}
