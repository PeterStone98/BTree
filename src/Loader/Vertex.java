/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.io.Serializable;

/**
 *
 * @author Pete
 */
public class Vertex implements Comparable<Vertex>, Serializable {

    public Website name;//link from prevous webiste to this one
    public double distance;//similarity between two websites

    public Vertex(Website dest, double d) {
        this.name = dest;
        this.distance = d;
    }

    @Override
    public int compareTo(Vertex o) {
        //use 100x to compare decimal similarities
        return (int) ((100 * this.distance) - (100 * o.distance));
    }

    @Override
    public String toString() {
        return name + ":" + distance;
    }
}
