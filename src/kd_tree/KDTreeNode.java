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
public class KDTreeNode {

    final int max_bucket_size = 20;
    PointEntry[] list_of_points;
    int divider_dimension;
    double divider_value;
    KDTreeNode leftChild;
    KDTreeNode rightChild;
    double[][] bounding_box;//[dimension][0] = min; [dimension][1] = max
    boolean is_leaf_node;
    private int last_point_index;

    public KDTreeNode(int dimension) {
        bounding_box = new double[dimension][2];
        last_point_index = 0;
        is_leaf_node = true;
        divider_dimension = -1;
        list_of_points = new PointEntry[max_bucket_size + 1];
        leftChild = null;
        rightChild = null;
    }

    //Get closest point. Return null if point is greater than best_distance away
    public PointEntry get_nearest_point(PointEntry p, double best_distance) {
        //Make sure this node could have a closer point
        if (best_distance < distanceSquaredToBox(p, bounding_box)) {
            return null;
        }
        
        if (is_leaf_node) {
            PointEntry best_p = null;
            for (int i = 0; i < last_point_index; i++) {
                if (distanceSquared(p, list_of_points[i]) < best_distance) {
                    best_distance = distanceSquared(p, list_of_points[i]);
                    best_p = list_of_points[i];
                }
            }
            return best_p;
        } else {
            KDTreeNode good_child = null;
            KDTreeNode bad_child = null;
            if (divider_value < p.pointCoordinates[divider_dimension]) {
                good_child = rightChild;
                bad_child = leftChild;
            } else {
                good_child = leftChild;
                bad_child = rightChild;
            }
            PointEntry p1 = good_child.get_nearest_point(p, best_distance);//Get nearest child in the right tree
            if (p1 != null) {
                double d = distanceSquared(p, p1); //Get distance to this child
                PointEntry p2 = bad_child.get_nearest_point(p, d);
                if (p2 != null) {
                    return p2;
                } else {
                    return p1;
                }
            } else {
                return bad_child.get_nearest_point(p, best_distance);
            }
        }
    }

    public void add_point(PointEntry p) {
        if (is_leaf_node) {
            //if a leaf node can fit the point, just add to the list
            if (last_point_index < max_bucket_size) {
                list_of_points[last_point_index] = p;
                last_point_index++;
            } else {
                //split the node if it is too big
                int split_dimension = 0;
                double max_dimension_range = 0;
                for (int i = 0; i < bounding_box.length; i++) {
                    double[] min_max = bounding_box[i];
                    double range = min_max[1] - min_max[0];
                    if (range > max_dimension_range) {
                        split_dimension = i;
                        max_dimension_range = range;
                    }
                }
                double split_value = 0;
                //TODO: improve this by using the median or some approximation
                split_value = (bounding_box[split_dimension][0] + bounding_box[split_dimension][1]) / 2;

                split(split_dimension, split_value);

            }
        } else {
            //if it's not a leaf node, go to the correct child
            if (p.pointCoordinates[divider_dimension] > divider_value) {
                rightChild.add_point(p);
            } else {
                leftChild.add_point(p);
            }
        }
        updateBoundingBox(p);
    }

    private void split(int dimension, double value) {
        assert is_leaf_node;//Throw error if not a leaf node

        leftChild = new KDTreeNode(bounding_box.length);
        rightChild = new KDTreeNode(bounding_box.length);

        for (int i = 0; i < last_point_index; i++) {
            PointEntry p = list_of_points[i];
            if (p.pointCoordinates[dimension] > value) {
                rightChild.add_point(p);
            } else {
                leftChild.add_point(p);
            }
        }

        is_leaf_node = false;
        last_point_index = 0;
        list_of_points = null;
        divider_dimension = dimension;
        divider_value = value;
    }

    private void updateBoundingBox(PointEntry p) {
        for (int i = 0; i < bounding_box.length; i++) {
            if (p.pointCoordinates[i] < bounding_box[i][0]) {
                bounding_box[i][0] = p.pointCoordinates[i];
            } else if (p.pointCoordinates[i] > bounding_box[i][1]) {
                bounding_box[i][1] = p.pointCoordinates[i];
            }
        }
    }

    public int getLastPointIndex() {
        return last_point_index;
    }

    public void setLastPointIndex(int index) {
        last_point_index = index;
    }

    //We use the squared distance because we only care about the relative distances between pairs of points
    private double distanceSquared(PointEntry p1, PointEntry p2) {
        double sum = 0.0;
        for (int i = 0; i < p1.pointCoordinates.length; i++) {
            sum += (p1.pointCoordinates[i] - p2.pointCoordinates[i]) * (p1.pointCoordinates[i] - p2.pointCoordinates[i]);
        }
        return sum;
    }

    //We use the squared distance because we only care about the relative distances between pairs of points
    private double distanceSquaredToBox(PointEntry p, double[][] box) {
        double sum = 0.0;
        for (int i = 0; i < p.pointCoordinates.length; i++) {
            if (p.pointCoordinates[i] < box[i][0]) {
                sum += (p.pointCoordinates[i] - box[i][0]) * (p.pointCoordinates[i] - box[i][0]);
            } else if (p.pointCoordinates[i] > box[i][1]) {
                sum += (p.pointCoordinates[i] - box[i][1]) * (p.pointCoordinates[i] - box[i][1]);
            } else {
                sum += 0;
            }
        }
        return sum;
    }

}
