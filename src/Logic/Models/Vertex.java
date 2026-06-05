package Logic.Models;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private CompanyStructure companyStructure;

    public Vertex() {
        this.companyStructure = null;
    }

    public CompanyStructure getCompanyStructure() {
        return companyStructure;
    }

    public void setCompanyStructure(CompanyStructure companyStructure) {
        this.companyStructure = companyStructure;
    }


//    private List<Sector> adjacentSectors;
//    private List<Edge> adjacentEdges;
//    public Vertex(List<Sector> adjacentSectors) {
//        if(adjacentSectors!=null) {this.adjacentSectors=adjacentSectors;}
//        else this.adjacentSectors=new ArrayList<>();
//        this.adjacentEdges=new ArrayList<>();
//        this.companyStructure = null;
//    }

//    public List<Sector> getAdjacentSectors() {
//        return adjacentSectors;
//    }
//    public void setAdjacentSectors(List<Sector> adjacentSectors) {
//        this.adjacentSectors = adjacentSectors;
//    }
//    public List<Edge> getAdjacentEdges() {
//        return adjacentEdges;
//    }
//    public void addAdjacentEdge(Edge edge) {
//        this.adjacentEdges.add(edge);
//    }
}
