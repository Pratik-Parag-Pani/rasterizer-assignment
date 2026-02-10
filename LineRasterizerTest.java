import processing.core.PApplet;

class Point {
    int x, y;
    float intensity = 1.0f; // Default for non-antialiased points

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(int x, int y, float intensity) {
        this.x = x;
        this.y = y;
        this.intensity = intensity;
    }
}

public class LineRasterizerTest extends PApplet {

    // Global variables
    final int GRID_SIZE = 50;
    final int CELL_SIZE = 10;
    final int CANVAS_SIZE = GRID_SIZE * CELL_SIZE;

    Point endpoint1;
    Point endpoint2;
    Point dragging = null;
    final int ENDPOINT_RADIUS = 8;

    LineRasterizer rasterizer;
    String rasterizerName;
    int mode = 0; // 0: Bresenham, 1: DDA, 2: Midpoint Circle, 3: Wu

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        // Initialize endpoints
        endpoint1 = new Point(10, 10);
        endpoint2 = new Point(40, 35);

        // Create rasterizer instance
        updateRasterizer();
    }

    void updateRasterizer() {
        switch (mode) {
            case 0:
                rasterizer = new BresenhamRasterizer();
                rasterizerName = "Bresenham";
                break;
            case 1:
                rasterizer = new DDARasterizer();
                rasterizerName = "DDA";
                break;
            case 2:
                rasterizer = new MidpointCircleRasterizer();
                rasterizerName = "Midpoint Circle";
                break;
            case 3:
                rasterizer = new WuRasterizer();
                rasterizerName = "Wu (Anti-aliased)";
                break;
        }
    }

    public void draw() {
        background(255);

        // Draw grid
        stroke(220);
        strokeWeight(1);
        for (int i = 0; i <= GRID_SIZE; i++) {
            line(i * CELL_SIZE, 0, i * CELL_SIZE, CANVAS_SIZE);
            line(0, i * CELL_SIZE, CANVAS_SIZE, i * CELL_SIZE);
        }

        // Get rasterized pixels
        Point[] rasterizedPixels = rasterizer.rasterize(endpoint1, endpoint2);

        // Draw rasterized pixels as filled cells
        noStroke();
        for (Point p : rasterizedPixels) {
            fill(100, 100, 255, p.intensity * 255);
            rect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw reference line (1px thin line)
        stroke(255, 0, 0);
        strokeWeight(1);
        line(endpoint1.x * CELL_SIZE + CELL_SIZE / 2,
                endpoint1.y * CELL_SIZE + CELL_SIZE / 2,
                endpoint2.x * CELL_SIZE + CELL_SIZE / 2,
                endpoint2.y * CELL_SIZE + CELL_SIZE / 2);

        // Draw endpoints
        fill(255, 100, 100);
        stroke(200, 50, 50);
        strokeWeight(2);
        ellipse(endpoint1.x * CELL_SIZE + CELL_SIZE / 2,
                endpoint1.y * CELL_SIZE + CELL_SIZE / 2,
                ENDPOINT_RADIUS * 2, ENDPOINT_RADIUS * 2);
        ellipse(endpoint2.x * CELL_SIZE + CELL_SIZE / 2,
                endpoint2.y * CELL_SIZE + CELL_SIZE / 2,
                ENDPOINT_RADIUS * 2, ENDPOINT_RADIUS * 2);

        // UI Text
        fill(0);
        textSize(16);
        textAlign(LEFT, TOP);
        text("Mode: " + rasterizerName + " (Press 'M' to switch)", 10, 10);
    }

    public void mousePressed() {
        // Check if clicking on endpoint1
        float d1 = dist(mouseX, mouseY,
                endpoint1.x * CELL_SIZE + CELL_SIZE / 2,
                endpoint1.y * CELL_SIZE + CELL_SIZE / 2);
        if (d1 < ENDPOINT_RADIUS) {
            dragging = endpoint1;
            return;
        }

        // Check if clicking on endpoint2
        float d2 = dist(mouseX, mouseY,
                endpoint2.x * CELL_SIZE + CELL_SIZE / 2,
                endpoint2.y * CELL_SIZE + CELL_SIZE / 2);
        if (d2 < ENDPOINT_RADIUS) {
            dragging = endpoint2;
            return;
        }
    }

    public void mouseDragged() {
        if (dragging != null) {
            // Convert mouse position to grid coordinates
            int gridX = constrain(mouseX / CELL_SIZE, 0, GRID_SIZE - 1);
            int gridY = constrain(mouseY / CELL_SIZE, 0, GRID_SIZE - 1);

            dragging.x = gridX;
            dragging.y = gridY;
        }
    }

    public void mouseReleased() {
        dragging = null;
    }

    public void keyPressed() {
        if (key == 'm' || key == 'M') {
            mode = (mode + 1) % 4;
            updateRasterizer();
        }
    }

    public static void main(String[] args) {
        PApplet.main("LineRasterizerTest");
    }
}