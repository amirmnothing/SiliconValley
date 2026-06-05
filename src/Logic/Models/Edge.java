package Logic.Models;


public class Edge {
    private Vertex start;
    private Vertex end;
    private PartnerShip partnership;
    public Edge(Vertex start, Vertex end) {
       this.start = start;
       this.end = end;
       this.partnership = null;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public PartnerShip getPartnership() {
        return partnership;
    }

    public void setPartnership(PartnerShip partnership) {
        this.partnership = partnership;
    }
}
