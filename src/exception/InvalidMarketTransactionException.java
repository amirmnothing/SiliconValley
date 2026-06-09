package exception;

import logic.enums.ResourceType;

public class InvalidMarketTransactionException extends RuntimeException {
    private final ResourceType resourceType;

    public InvalidMarketTransactionException( ResourceType resourceType,String message)
    {
        super(message);
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
