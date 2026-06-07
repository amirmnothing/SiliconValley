package logic.models;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private CompanyStructure companyStructure;
    private List<Edge> adjacentEdges;

    public Vertex() {
        this.companyStructure = null;
        this.adjacentEdges = new ArrayList<>();
    }

    public CompanyStructure getCompanyStructure() {
        return companyStructure;
    }

    public void setCompanyStructure(CompanyStructure companyStructure) {
        this.companyStructure = companyStructure;
    }

    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }

    public void addAdjacentEdge(Edge edge) {
        if (edge != null && !adjacentEdges.contains(edge))
            this.adjacentEdges.add(edge);
    }

}
