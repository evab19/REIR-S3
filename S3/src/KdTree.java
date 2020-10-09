
/*************************************************************************
 *************************************************************************/


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node root;
    private int size;


    // construct an empty set of points
    public KdTree() {
        size = 0;
        root = null;
    }

    private class Node {
        private Point2D p;
        private RectHV rectangle;
        private Node left;
        private Node right;

        public Node(Point2D point, RectHV rectangle) {
            this.p = point;
            this.rectangle = rectangle;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        RectHV rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);
        root = insert(root, p, rectangle, true);
    }

    private Node insert(Node node, Point2D p, RectHV rectangle, boolean vertical) {
        //Insert new point when you reach an empty location
        if (node == null) {
            size++;
            return new Node(p, rectangle);
        }
        //If the point already exists, return to avoid duplicates
        if (node.p.equals(p)) return node;

        //If the last point was vertical, check the x
        if (vertical) {
            if (p.x() < node.p.x()) {
                //Go left
                rectangle = new RectHV(rectangle.xmin(), rectangle.ymin(), node.p.x(), rectangle.ymax());
                node.left = insert(node.left, p, rectangle, false);
            } else {
                //Go right
                rectangle = new RectHV(node.p.x(), rectangle.ymin(), rectangle.xmax(), rectangle.ymax());
                node.right = insert(node.right, p, rectangle, false);
            }
        }
        //If the last point was horizontal, check the y
        else {
            if (p.y() < node.p.y()) {
                //Go left
                rectangle = new RectHV(rectangle.xmin(), rectangle.ymin(), rectangle.xmax(), node.p.y());
                node.left = insert(node.left, p, rectangle, true);
            } else {
                //Go right
                rectangle = new RectHV(rectangle.xmin(), node.p.y(), rectangle.xmax(), rectangle.ymax());
                node.right = insert(node.right, p, rectangle, true);
            }
        }
        return node;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    // Recursive contains helper function
    private boolean contains(Node node, Point2D point, boolean vertical) {
        // If tree is empty or if we reach the end of the tree without finding the node
        if (node == null) {
            return false;
        }
        // If we find a matching point in the tree
        else if (node.p.equals(point)) {
            return true;
        } else {
            // Figure out if we are going left or right from the current node
            if (vertical) {
                if (point.x() < node.p.x()) {
                    return contains(node.left, point, false);
                } else {
                    return contains(node.right, point, false);
                }
            } else {
                if (point.y() < node.p.y()) {
                    return contains(node.left, point, true);
                } else {
                    return contains(node.right, point, true);
                }
            }
        }
    }

    // draw all of the points to standard draw
    public void draw() {

    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        range(rect, root, points);
        return points;
    }

    private void range(RectHV rect, Node node, ArrayList<Point2D> points) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.p)) {
            points.add(node.p);
        }
        if (rect.intersects(node.rectangle)) {
            range(rect, node.left, points);
            range(rect, node.right, points);
        }
    }

    public Point2D nearest(Point2D point) {
        if (root == null) return null;
        Point2D nearest = nearest(root, point, root.p, true);
        return nearest;
    }

    private Point2D nearest(Node node, Point2D point, Point2D n, boolean vertical) {
        Point2D nearest = n;
        if (node == null) {
            return nearest;
        }

        if (node.p.distanceSquaredTo(point) < nearest.distanceSquaredTo(point)) {
            nearest = node.p;
        }

        if (node.rectangle.distanceSquaredTo(point) < nearest.distanceSquaredTo(point)) {
            nearest = nearest(node.left, point, nearest, !vertical);
            nearest = nearest(node.right, point, nearest, !vertical);
        }
        return nearest;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/


    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        Out out = new Out();
        int N = in.readInt(), C = in.readInt(), T = 50;
        Point2D[] queries = new Point2D[C];
        KdTree tree = new KdTree();
        out.printf("Inserting %d points into tree\n", N);
        for (int i = 0; i < N; i++) {
            tree.insert(new Point2D(in.readDouble(), in.readDouble()));
        }
        out.printf("tree.size(): %d\n", tree.size());
        out.printf("Testing `nearest` method, querying %d points\n", C);

        for (int i = 0; i < C; i++) {
            queries[i] = new Point2D(in.readDouble(), in.readDouble());
            out.printf("%s: %s\n", queries[i], tree.nearest(queries[i]));
        }
        for (int i = 0; i < T; i++) {
            for (int j = 0; j < C; j++) {
                tree.nearest(queries[j]);
            }
        }
    }
}
