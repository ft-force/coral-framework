package com.ftf.coral.core.exception;

public class ResourceNotExistException extends BusinessException {

    private static final long serialVersionUID = 1L;

    private String resourceType;

    private String resourceId;

    public ResourceNotExistException(final String message) {
        super(message);
    }

    public ResourceNotExistException(final String resourceType, final String resourceId) {
        super(resourceType + " not exists. ResourceId=" + resourceId);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotExistException(final String resourceType, final String resourceId, final String message) {
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
