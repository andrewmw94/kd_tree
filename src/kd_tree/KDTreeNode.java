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

}
