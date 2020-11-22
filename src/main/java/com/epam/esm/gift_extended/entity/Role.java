package com.epam.esm.gift_extended.entity;

public enum Role {
    GUEST("GUEST"),USER("USER"),ADMIN("ADMIN");
    String roleAsString;

    Role(String roleAsString) {
        this.roleAsString = roleAsString;
    }

    public String getRoleAsString() {
        return roleAsString;
    }
}
