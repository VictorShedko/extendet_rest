package com.epam.esm.gift_extended.exception;

public class UniqFieldException extends GiftException {

    private String uniqFields;

    public UniqFieldException(String uniqFields) {
        this.uniqFields = uniqFields;
    }

    public String getUniqFields() {
        return uniqFields;
    }

}
