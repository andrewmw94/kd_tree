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
    double[][] bounding_box;//[dimension][0] = min; [dimension][1] = max
    boolean is_leaf_node;  
    
    
    private int last_point_index;
    
    public KDTreeNode(int dimension) {
        bounding_box = new double[dimension][2];
        last_point_index = 0;
        is_leaf_node = true;
        divider_dimension = -1;
        list_of_points = new PointEntry[max_bucket_size];
    }
    
    
    public void add_point(PointEntry p) {
        if(last_point_index < max_bucket_size) {
            list_of_points[last_point_index] = p;
            last_point_index++;
        }
    }
    
    public int getLastPointIndex() {
        return last_point_index;
    }

}
