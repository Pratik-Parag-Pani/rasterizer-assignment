import java.util.ArrayList;
import java.util.List;

class DDARasterizer implements LineRasterizer {
    @Override
    public Point[] rasterize(Point p1, Point p2) {
        List<Point> points = new ArrayList<>();
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;

        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            points.add(new Point(x1, y1));
            return points.toArray(new Point[0]);
        }

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            points.add(new Point(Math.round(x), Math.round(y)));
            x += xIncrement;
            y += yIncrement;
        }

        return points.toArray(new Point[0]);
    }
}