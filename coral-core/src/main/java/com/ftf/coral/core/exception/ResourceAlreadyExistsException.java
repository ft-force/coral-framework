package com.ftf.coral.core.exception;

public class ResourceAlreadyExistsException extends BusinessException {

    private static final long serialVersionUID = 1L;

    private String resourceType;

    private String resourceId;

    public ResourceAlreadyExistsException(final String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(final String resourceType, final String resourceId) {
        super(resourceType + " already exists. ResourceId=" + resourceId);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceAlreadyExistsException(final String resourceType, final String resourceId, final String message) {
        super(message);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
