import java.util.ArrayList;
import java.util.List;

class WuRasterizer implements LineRasterizer {
    @Override
    public Point[] rasterize(Point p1, Point p2) {
        List<Point> points = new ArrayList<>();

        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;

        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);

        if (steep) {
            int temp = x1;
            x1 = y1;
            y1 = temp;

            temp = x2;
            x2 = y2;
            y2 = temp;
        }

        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;
        float gradient = (dx == 0) ? 1.0f : (float) dy / dx;

        if (steep) {
            points.add(new Point(y1, x1));
            points.add(new Point(y2, x2));
        } else {
            points.add(new Point(x1, y1));
            points.add(new Point(x2, y2));
        }

        float intery = y1 + gradient;

        for (int x = x1 + 1; x < x2; x++) {
            int py = (int) Math.floor(intery);

            if (steep) {
                points.add(new Point(py, x));
                points.add(new Point(py + 1, x));
            } else {
                points.add(new Point(x, py));
                points.add(new Point(x, py + 1));
            }

            intery += gradient;
        }

        return points.toArray(new Point[0]);
    }
}
