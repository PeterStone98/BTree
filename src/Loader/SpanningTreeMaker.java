/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author Pete
 * This class creates the spanning trees out of the webList
 * it takes the entire webList and does a breadth first search to get all connected websites and turns it into a spanning tree
 * it then removes all the websites in the spanning tree from the webList and repeats until all spanning trees are made
 */
public class SpanningTreeMaker {

    //I just do a depth first search to organize the websites into spanning trees
    ArrayList<Website> webList;

    public SpanningTreeMaker(ArrayList<Website> list) {
        webList = list;
    }

    
    //This method is the BFS that groups all websites that are connected to a starting website
    ArrayList<Website> spanningTree(Website a) {
        //search trhough all connected websites and add to a list
        ArrayDeque<Website> pool = new ArrayDeque<>();
        ArrayList<Website> list = new ArrayList<>();//list of wesbites to return 
        pool.addLast(a);
        list.add(a);
        while (!pool.isEmpty()) {
            Website p = pool.removeLast();
            if (p.visited == false) {
                p.visited = true;
                for (Website w : p.neighbors) {
                    //add all neighbors
                    pool.addLast(w);
                    if (!list.contains(w)) {
                        list.add(w);
                    }
                }
            }
        }
        return list;
    }

    //This method starts with the entire webList and uses the spanningTree method to create spanning trees
    //it collects the spanning tree and then removes all of its websites from the webList and repeats until all spanning trees are made
    //it then returns the array of spanning trees
    ArrayList<Website>[] getSpanningTress() {
        ArrayList<Website> list = new ArrayList<>();//list of spanning tree websites
        ArrayList<Website>[] output = new ArrayList[5];//array of spanning trees(Max size of five becasue I start with 5 seperate urls)
        //initialize the spanning trees
        for (int i = 0; i < output.length; i++) {
            ArrayList<Website> spanTree = new ArrayList<>();
            output[i] = spanTree;
        }

        int n = 0;
        ArrayList<Website> total = webList;
        Website start;
        //start with first website and get its spanning tree
        while (!total.isEmpty()) {
            start = total.get(0);
            list = this.spanningTree(start);
            for (Website w : list) {
                //remove websites from total and start again
                total.remove(w);
                output[n].add(w);
            }
            n++;
        }
        return output;
    }

}
