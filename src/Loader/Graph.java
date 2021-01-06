/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author Pete
 * This class gets the shortest path from one node to the other
 */
public class Graph implements Serializable {

    private final Map<Website, List<Vertex>> vertices;//graph for traversing
    public ArrayList<Website> webList = new ArrayList<>();

    public Graph() {
        this.vertices = new HashMap<>();
    }

    //add a website and all its neighbors(edges) to the graph
    public void addVertex(Website website, List<Vertex> vertexs) {
        this.vertices.put(website, vertexs);
    }

    //This method finds the shirtest path between two websites using Dijkstra's algorithm
    public List<Website> Dijkstra(Website start, Website finish) {
        final Map<Website, Double> distances = new HashMap<>();//graph of distances
        final Map<Website, Vertex> previous = new HashMap<>();//graph of previous websites
        PriorityQueue<Vertex> nodes = new PriorityQueue<>();//priority queue

        //for every vertex(Website) in the graph add it to nodes and set distance to max
        for (Website vertex : vertices.keySet()) {
            //if its the starting website set the distance to 0 since its the start of the path
            if (vertex == start) {
                distances.put(vertex, 0.0);
                nodes.add(new Vertex(vertex, 0));
            } else {
                //add vertex with "inifinty distance"
                distances.put(vertex, Double.MAX_VALUE);
                nodes.add(new Vertex(vertex, Double.MAX_VALUE));
            }
            previous.put(vertex, null);
        }

        while (!nodes.isEmpty()) {
            //get the vertex with the smallest distance using poll method
            Vertex smallest = nodes.poll();
            //if it is the finsh then return the current path
            if (smallest.name == finish) {
                List<Website> path = new ArrayList<>();
                //for each vertex in previous add to list
                Vertex v = smallest;
                while (previous.get(v.name) != null) {
                    path.add(v.name);
                    v = previous.get(v.name);
                }
                //return path
                return path;
            }

            if (distances.get(smallest.name) == Double.MAX_VALUE) {
                //break because its not a branch to the destination
                break;
            }

            //if smallest vertex does equal finish then add all the neighbors into
            for (Vertex neighbor : vertices.get(smallest.name)) {
                //get the distance of smallest + its neighbor
                double alt = distances.get(smallest.name) + neighbor.distance;
                //if alt less then the current distance
                if (alt < distances.get(neighbor.name)) {
                    //add new distance and add vertex to previous
                    distances.put(neighbor.name, alt);
                    previous.put(neighbor.name, smallest);
                    //change the distance of the vertex to decrease its priority
                    for (Vertex n : nodes) {
                        if (n.name == neighbor.name) {
                            nodes.remove(n);
                            n.distance = alt;
                            nodes.add(n);
                            break;
                        }
                    }
                }
            }
        }
        //failed, return null graph
        return null;
    }
}
