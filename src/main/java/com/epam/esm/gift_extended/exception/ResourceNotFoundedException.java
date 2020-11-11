package com.epam.esm.gift_extended.exception;

public class ResourceNotFoundedException extends GiftException {

    private String resourceName;
    private String requestedValues;

    public ResourceNotFoundedException(String resourceName, String requestedValues) {
        this.resourceName = resourceName;
        this.requestedValues = requestedValues;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getRequestedValues() {
        return requestedValues;
    }
}
