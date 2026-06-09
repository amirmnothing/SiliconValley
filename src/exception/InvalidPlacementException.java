package exception;

import logic.models.Edge;
import logic.models.Vertex;

public class InvalidPlacementException extends RuntimeException {
    private final Vertex vertex;
    private final Edge edge;


    //برای MPV
    public InvalidPlacementException(Vertex vertex, String message) {
        super(message);
        this.vertex = vertex;
        this.edge = null;
    }

    //برای Partnership
    public InvalidPlacementException(Edge edge, String message) {
        super(message);
        this.edge = edge;
        this.vertex = null;
    }
    public Vertex getVertex() {
        return vertex;
    }
    public Edge getEdge() {
        return edge;
    }

}
