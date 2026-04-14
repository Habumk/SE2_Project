package com.klearn.exception;

public class ResourceNotFoundException extends KLearnException {
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(
            String.format("%s not found: %s", resourceName, identifier),
            "RESOURCE_NOT_FOUND"
        );
    }
}
