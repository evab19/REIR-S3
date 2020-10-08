
/*************************************************************************
 *************************************************************************/

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

public class KdTree {

    private Node root;
    private int size;


    // construct an empty set of points
    public KdTree() {
        size = 0;
        root = null;
    }

    private class Node {
        // Það sem Node vill er parent, orientation
        private Point2D p;
        private boolean vertical;
        private RectHV rectangle;
        private Node left;
        private Node right;

        public Node(Point2D point, RectHV rectangle) {
            this.p = point;
            this.vertical = true;
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
    };

    private Node insert(Node node, Point2D p, RectHV rectangle, boolean vertical) {
        //Insert new point when you reach an empty location
        if (node == null){
            size++;
            return new Node(p, rectangle);
        }
        //If the point already exists, return to avoid duplicates
        if(node.p.equals(p)) return node;

        //If the last point was vertical, check the x
        if(node.vertical) {
            if(p.x() < node.p.x()){
                //Go left
                rectangle.xmax = node.p.x();
                node.left = insert(node.left, p, rectangle ,false);
            }
            else{
                //Go right
                rectangle.xmin = node.p.x();
                node.right = insert(node.right, p, rectangle, false);
            }
        }
        //If the last point was horizontal, check the y
        else{
            if(p.y() < node.p.y()){
                //Go left
                rectangle.ymax = node.p.y();
                node.left = insert(node.left, p, rectangle, false);
            }
            else{
                //Go right
                rectangle.ymin = node.p.y();
                node.right = insert(node.right, p, rectangle, true);
            }
        }

    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return false;
    }

    // draw all of the points to standard draw
    public void draw() {

    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return p;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int nrOfRecangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRecangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRecangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        KdTree set = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRecangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }

        out.println();
    }
}
