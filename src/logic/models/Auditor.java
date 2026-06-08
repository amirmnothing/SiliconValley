package logic.models;

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

    public void moveAuditor(Sector newSector) {
        if (newSector == null || newSector == this.currentSector) {
            //TODO با اکسپشن هندل کرد
            return;
        }

        if (this.currentSector != null) {
            this.currentSector.setAuditor(false);
        }
        this.currentSector = newSector;
        this.currentSector.setAuditor(true);
    }
}