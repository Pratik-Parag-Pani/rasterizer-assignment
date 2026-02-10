import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MidpointCircleRasterizer implements LineRasterizer {
    @Override
    public Point[] rasterize(Point p1, Point p2) {
        // Calculate center and radius from diameter endpoints p1 and p2
        float centerX = (p1.x + p2.x) / 2.0f;
        float centerY = (p1.y + p2.y) / 2.0f;
        float radius = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)) / 2.0f;

        int cx = Math.round(centerX);
        int cy = Math.round(centerY);
        int r = Math.round(radius);

        Set<PointKey> pointSet = new HashSet<>();
        int x = r;
        int y = 0;
        int err = 1 - r;

        while (x >= y) {
            addOctantPoints(pointSet, cx, cy, x, y);
            y++;
            if (err <= 0) {
                err += 2 * y + 1;
            } else {
                x--;
                err += 2 * (y - x) + 1;
            }
        }

        List<Point> points = new ArrayList<>();
        for (PointKey pk : pointSet) {
            points.add(new Point(pk.x, pk.y));
        }
        return points.toArray(new Point[0]);
    }

    private void addOctantPoints(Set<PointKey> points, int cx, int cy, int x, int y) {
        points.add(new PointKey(cx + x, cy + y));
        points.add(new PointKey(cx + y, cy + x));
        points.add(new PointKey(cx - y, cy + x));
        points.add(new PointKey(cx - x, cy + y));
        points.add(new PointKey(cx - x, cy - y));
        points.add(new PointKey(cx - y, cy - x));
        points.add(new PointKey(cx + y, cy - x));
        points.add(new PointKey(cx + x, cy - y));
    }

    // Helper class to handle Point uniqueness in a Set
    private static class PointKey {
        int x, y;

        PointKey(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof PointKey))
                return false;
            PointKey pointKey = (PointKey) o;
            return x == pointKey.x && y == pointKey.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }
}