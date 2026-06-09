package exception;

import logic.models.Sector;

public class InvalidAuditorMovementException extends RuntimeException {
    private final Sector sourceSector;
    private final Sector destinationSector;
    public InvalidAuditorMovementException(String message, Sector sourceSector, Sector destinationSector)
    {
        super(message);
        this.sourceSector = sourceSector;
        this.destinationSector = destinationSector;
    }

    public Sector getSourceSector() {
        return sourceSector;
    }

    public Sector getDestinationSector() {
        return destinationSector;
    }
}
