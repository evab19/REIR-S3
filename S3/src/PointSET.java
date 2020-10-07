
/****************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    
 *  Dependencies:
 *  Author:
 *  Date:
 *
 *  Data structure for maintaining a set of 2-D points, 
 *    including rectangle and nearest-neighbor queries
 *
 *************************************************************************/


import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class PointSET {

    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.size() == 0;
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> points_inside = new ArrayList<>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                points_inside.add(point);
            }
        }
        return points_inside;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if (set.isEmpty()) {
            return null;
        }
        Point2D neighbour = set.min();
        for (Point2D point : set) {
            if (p.distanceSquaredTo(point) < p.distanceSquaredTo(neighbour)) {
                neighbour = point;
            }
        }
        return neighbour;
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();

        for (int i = 0; i < args.length; i = i+2 ) {
            set.insert(new Point2D(Double.parseDouble(args[i]), Double.parseDouble(args[i+1])));
        }
    }
}
