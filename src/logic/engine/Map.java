package logic.engine;

import logic.enums.CornerDirection;
import logic.enums.ResourceType;
import logic.models.Edge;
import logic.models.Sector;
import logic.models.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
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

    private void initMap(){

        Random random = new Random();
        // ساخت همه گره ها و قرار دادن آنها در آرایه دو بعدی گره ها
        for (int r = 0; r <= rows; r++){
            for (int c = 0; c <= cols; c++){
                vertices[r][c] = new Vertex();
            }
        }

        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){

                // TODO : الگوریتم رندوم برای خونه ها باید هوشمند سازی بشه تا تعداد هر کدوم از انواع سکتور ها، منطقی باشد
                ResourceType RT = ResourceType.values()[random.nextInt(6)];
                sectors[r][c] = new Sector(RT ,
                        RT == ResourceType.REGULATORY ? 0 : random.nextInt(11) + 2 ,
                        false);

                // اتصال 4 گره اطراف هر سکتور به آن
                sectors[r][c].setCorner(CornerDirection.TOP_LEFT, vertices[r][c]);
                sectors[r][c].setCorner(CornerDirection.TOP_RIGHT, vertices[r][c + 1]);
                sectors[r][c].setCorner(CornerDirection.BOTTOM_LEFT, vertices[r + 1][c]);
                sectors[r][c].setCorner(CornerDirection.BOTTOM_RIGHT, vertices[r + 1][c + 1]);
            }
        }

        for (int r = 0; r <= rows; r++){
            for (int c = 0; c <= cols; c++){

                // تولید یال های عمودی
                if (r < rows)
                    edges.add(new Edge(vertices[r][c],vertices[r + 1][c]));

                // تولید یال های افقی
                if (c < cols)
                    edges.add(new Edge(vertices[r][c],vertices[r][c + 1]));
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
