package logic.engine;

import logic.enums.CornerDirection;
import logic.enums.ResourceType;
import logic.models.Edge;
import logic.models.Sector;
import logic.models.Vertex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map implements Serializable {
    private final int rows;
    private final int cols;

    private Sector[][] sectors;
    private Vertex[][] vertices;
    private List<Edge> edges;

    public Map(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        this.sectors = new Sector[rows][cols];
        this.vertices = new Vertex[rows + 1][cols + 1];
        this.edges = new ArrayList<>();

        initMap();
    }

    private void initMap() {

        Random random = new Random();

        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {
                vertices[r][c] = new Vertex();
            }
        }
        int totalSectors = rows * cols;
        int capitalCount = (int) (totalSectors * 0.4);
        int regulatoryCount = (int) (totalSectors * 0.08);
        int remainingCount = totalSectors - capitalCount - regulatoryCount;
        List<ResourceType> resourceTypes = new ArrayList<>();
        for (int i = 0; i < capitalCount; i++) {
            resourceTypes.add(ResourceType.CAPITAL);
        }
        for (int i = 0; i < regulatoryCount; i++) {
            resourceTypes.add(ResourceType.REGULATORY);
        }
        ResourceType[] otherTypes = {ResourceType.TALENT, ResourceType.CLOUD, ResourceType.PATENT, ResourceType.DATA};
        for (int i = 0; i < remainingCount; i++) {
            resourceTypes.add(otherTypes[random.nextInt(otherTypes.length)]);
        }
        Collections.shuffle(resourceTypes, random);
        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ResourceType RT = resourceTypes.get(index);
                index++;
                sectors[r][c] = new Sector(RT, RT == ResourceType.REGULATORY ? 0 : random.nextInt(11) + 2, false);

                sectors[r][c].setCorner(CornerDirection.TOP_LEFT, vertices[r][c]);
                sectors[r][c].setCorner(CornerDirection.TOP_RIGHT, vertices[r][c + 1]);
                sectors[r][c].setCorner(CornerDirection.BOTTOM_LEFT, vertices[r + 1][c]);
                sectors[r][c].setCorner(CornerDirection.BOTTOM_RIGHT, vertices[r + 1][c + 1]);

            }
        }

        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {

                if (r < rows)
                    edges.add(new Edge(vertices[r][c], vertices[r + 1][c]));

                if (c < cols)
                    edges.add(new Edge(vertices[r][c], vertices[r][c + 1]));
            }
        }
    }

    public Sector[][] getSectors() {
        return sectors;
    }

    public Vertex[][] getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}
