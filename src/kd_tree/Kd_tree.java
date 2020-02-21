/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kd_tree;

/**
 *
 * @author awells
 */
public class Kd_tree {

    KDTreeNode root_node;

    public Kd_tree(int dimension) {
        root_node = new KDTreeNode(dimension);
    }

    public void addPoint(PointEntry p) {
        KDTreeNode curr_node = root_node;
        if (curr_node.is_leaf_node) {
            curr_node.add_point(p);
        }
    }

    public PointEntry getNearestPoint(PointEntry p) {
        KDTreeNode curr_node = root_node;

        double best_distance = Double.POSITIVE_INFINITY;
        PointEntry best_point = null;

        if (curr_node.is_leaf_node) {
            for (int i = 0; i < curr_node.getLastPointIndex(); i++) {
                PointEntry other_p = curr_node.list_of_points[i];
                double dist = distanceSquared(p, other_p);
                if (dist < best_distance) {
                    best_distance = dist;
                    best_point = other_p;
                }
            }
            return best_point;
        }

        double[] arr = {0.0, 0.0};
        PointEntry return_point = new PointEntry(arr, arr);
        return return_point;
    }

    //We use the squared distance because we only care about the relative distances between pairs of points
    private double distanceSquared(PointEntry p1, PointEntry p2) {
        double sum = 0.0;
//        System.out.println(p1.pointCoordinates.length);
//        printPoint(p1);
//        printPoint(p2);
        for (int i = 0; i < p1.pointCoordinates.length; i++) {
            sum += (p1.pointCoordinates[i] - p2.pointCoordinates[i]) * (p1.pointCoordinates[i] - p2.pointCoordinates[i]);
        }
        return sum;
    }



}
