package Logic.Engine;

import Logic.Enums.CornerDirection;
import Logic.Models.Edge;
import Logic.Models.Sector;
import Logic.Models.Vertex;

import java.util.ArrayList;
import java.util.List;

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

        // ساخت همه گره ها و قرار دادن آنها در آرایه دو بعدی گره ها
        for (int r = 0; r <= rows; r++){
            for (int c = 0; c <= cols; c++){
                vertices[r][c] = new Vertex();
            }
        }

        for (int r = 0; r <= rows; r++){
            for (int c = 0; c <= cols; c++){

                // TODO تولید تصادفی نوع منبع و عدد تاس و ایجاد سکتور
                sectors[r][c] = new Sector();


                // اتصال 4 گره اطراف هر سکتور به آن
                sectors[r][c].setCorner(CornerDirection.TOP_LEFT, vertices[r][c]);
                sectors[r][c].setCorner(CornerDirection.TOP_RIGHT, vertices[r + 1][c]);
                sectors[r][c].setCorner(CornerDirection.BOTTOM_LEFT, vertices[r][c + 1]);
                sectors[r][c].setCorner(CornerDirection.BOTTOM_RIGHT, vertices[r + 1][c + 1]);
            }
        }

        // TODO ساخت یال ها

    }
}
