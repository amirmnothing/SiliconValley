package logic.models;


public class Edge {
    private Vertex start;
    private Vertex end;
    private Partnership partnership;

    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
        this.partnership = null;

        if (start != null) {
            start.addAdjacentEdge(this);
        }
        if (end != null) {
            end.addAdjacentEdge(this);
        }
    }

    public Vertex getOppositeVertex(Vertex current){
        if (current == start) return end;
        if (current == end) return start;
        else return null;
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

    public Partnership getPartnership() {
        return partnership;
    }

    public void setPartnership(Partnership partnership) {
        this.partnership = partnership;
    }
}
