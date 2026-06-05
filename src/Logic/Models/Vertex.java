package Logic.Models;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private List<Sector> adjacentSectors;
    private List<Edge> adjacentEdges;
    private CompanyStructure companyStructure;

    public Vertex(List<Sector> adjacentSectors) {
        if(adjacentSectors!=null) {this.adjacentSectors=adjacentSectors;}
        else this.adjacentSectors=new ArrayList<>();
        this.adjacentEdges=new ArrayList<>();
        this.companyStructure = null;
    }

    public CompanyStructure getCompanyStructure() {
        return companyStructure;
    }

    public void setCompanyStructure(CompanyStructure companyStructure) {
        this.companyStructure = companyStructure;
    }

    public List<Sector> getAdjacentSectors() {
        return adjacentSectors;
    }

    public void setAdjacentSectors(List<Sector> adjacentSectors) {
        this.adjacentSectors = adjacentSectors;
    }
    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }

    public void addAdjacentEdge(Edge edge) {
        this.adjacentEdges.add(edge);
    }
}
