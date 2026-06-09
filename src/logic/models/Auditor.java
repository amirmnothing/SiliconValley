package logic.models;

import exception.InvalidAuditorMovementException;

public class Auditor {
    private Sector currentSector;

    public Auditor(Sector initialSector) {
        currentSector = initialSector;
        if (currentSector != null) {
            currentSector.setAuditor(true);
        }
    }

    public Sector getCurrentSector() {
        return currentSector;
    }



    public void moveAuditor(Sector targetSector) {
        if(targetSector==null){
            throw new InvalidAuditorMovementException(
                    "Target sector cannot be null.",
                    currentSector,
                    null);
        }
        if (targetSector == this.currentSector) {
            throw new InvalidAuditorMovementException(
                    "The auditor must be moved to a different sector. It cannot stay in the same place!"
                    ,this.currentSector,targetSector);
        }

        if (this.currentSector != null) {
            this.currentSector.setAuditor(false);
        }
        this.currentSector = targetSector;
        this.currentSector.setAuditor(true);

    }
}