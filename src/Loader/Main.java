/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import Application.GUI;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Pete
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println("Loading...");
        ArrayList<Website> webList = new ArrayList<>();//array of all websites
        File file = new File("graphs.ser");//file to write to
        ArrayList<String> webNames = new ArrayList<>();
        ArrayList<Website> list;
        String[] mediods = {"Computer - Wikipedia" ,"Stan Lee - Wikipedia" ,"Education - Wikipedia" ,"Egypt - Wikipedia" ,"England - Wikipedia"};

        //iterate through websites and create objects from hyperlinks
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Computer").get();
        list = getWebsites(doc);
        for (Website w : list) {
            if (!webNames.contains(w.title)) {
                //if graph doesnt contain website add it(checking by website name)
                webNames.add(w.title);
                webList.add(w);
            } else {
                //get already made website and add new neighbors
                Website temp = webList.get(webNames.indexOf(w.title));
                for (String s : w.neighborsNames) {
                    if (!temp.neighborsNames.contains(s)) {
                        temp.neighborsNames.add(s);
                    }
                }
            }
        }

        doc = Jsoup.connect("https://en.wikipedia.org/wiki/Avengers_(comics)").get();
        list = getWebsites(doc);
        for (Website w : list) {
            if (!webNames.contains(w.title)) {
                //if graph doesnt contain website add it(checking by website name)
                webNames.add(w.title);
                webList.add(w);
            } else {
                //get already made website and add new neighbors
                Website temp = webList.get(webNames.indexOf(w.title));
                for (String s : w.neighborsNames) {
                    if (!temp.neighborsNames.contains(s)) {
                        temp.neighborsNames.add(s);
                    }
                }
            }
        }

        doc = Jsoup.connect("https://en.wikipedia.org/wiki/School").get();
        list = getWebsites(doc);
        for (Website w : list) {
            if (!webNames.contains(w.title)) {
                //if graph doesnt contain website add it(checking by website name)
                webNames.add(w.title);
                webList.add(w);
            } else {
                //get already made website and add new neighbors
                Website temp = webList.get(webNames.indexOf(w.title));
                for (String s : w.neighborsNames) {
                    if (!temp.neighborsNames.contains(s)) {
                        temp.neighborsNames.add(s);
                    }
                }
            }
        }
        doc = Jsoup.connect("https://en.wikipedia.org/wiki/Egypt").get();
        list = getWebsites(doc);
        for (Website w : list) {
            if (!webNames.contains(w.title)) {
                //if graph doesnt contain website add it(checking by website name)
                webNames.add(w.title);
                webList.add(w);
            } else {
                //get already made website and add new neighbors
                Website temp = webList.get(webNames.indexOf(w.title));
                for (String s : w.neighborsNames) {
                    if (!temp.neighborsNames.contains(s)) {
                        temp.neighborsNames.add(s);
                    }
                }
            }
        }
        doc = Jsoup.connect("https://en.wikipedia.org/wiki/England").get();
        list = getWebsites(doc);
        for (Website w : list) {
            if (!webNames.contains(w.title)) {
                //if graph doesnt contain website add it(checking by website name)
                webNames.add(w.title);
                webList.add(w);
            } else {
                //get already made website and add new neighbors
                Website temp = webList.get(webNames.indexOf(w.title));
                for (String s : w.neighborsNames) {
                    if (!temp.neighborsNames.contains(s)) {
                        temp.neighborsNames.add(s);
                    }
                }
            }
        }

        //make neighbors for all websites
        for (Website web : webList) {
            //check to see if its amediod from assignment 2
            if (Arrays.asList(mediods).contains(web.title)) {
                web.mediod = true;
            }
            //make neigbors
            for (String s : web.neighborsNames) {
                //I originally used just the titles because I didn't want to duplicate a website
                Website w = webList.get(webNames.indexOf(s));
                web.neighbors.add(w);
            }
        }

        //create different spanning trees out of weblist
        SpanningTreeMaker maker = new SpanningTreeMaker(webList);
        //this array list holds the different spanning trees
        ArrayList<Website>[] spanningTrees = maker.getSpanningTress();
        //create an array the same size as the spanning tree array
        Graph[] graphs = new Graph[spanningTrees.length];

        //for each spanning tree make a graph and place it in the graph array
        for (int i = 0; i < spanningTrees.length; i++) {
            ArrayList<Website> stree = spanningTrees[i];
            Graph g = new Graph();
            for (int j = 0; j < stree.size(); j++) {
                Website w = stree.get(j);
                //for each website add a vertex with attached vertexs made from neighbors
                ArrayList<Vertex> vertexs = new ArrayList<>();
                for (Website w1 : w.neighbors) {
                    Vertex v = new Vertex(w1, getCost(w, w1));
                    vertexs.add(v);
                }
                //add the website with all of its vertexs(neighbors)
                g.addVertex(w, vertexs);
                g.webList.add(w);
            }
            //put graph into array
            graphs[i] = g;
        }

        //write graph array trees to file
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(graphs);
        out.flush();
        out.close();

        //Inistialize GUI
        GUI gui = new GUI();
        gui.setVisible(true);
        System.out.println("Finsihed.");
    }

    static boolean test(Element link) {
        String s = link.attr("abs:href");
        return !s.contains("Portal:") && !s.contains("Talk:") && !s.contains("List_of") && !s.contains("Index_of") && !s.contains("Wikipedia:") && !s.contains("Help:") && !s.contains("Category:") && !s.contains(".jpg") && !s.contains(".JPG") && !s.contains("File:") && !s.contains("(");
    }

    static ArrayList<Website> getWebsites(Document doc) throws IOException, ClassNotFoundException {
        int breakPoint = 10;
        ArrayList<Website> webList = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        String text = doc.body().text();
        Website web = new Website(doc);
        Website parent1;
        Website parent2;
        webList.add(web);

        //iterate through links on page and create new website objs
        Elements linksOnPage = doc.select("#bodyContent a[href^=\"/wiki/\"]");
        int num = 0;
        parent1 = web;
        for (Element link : linksOnPage) {
            if (test(link)) {
                num++;
                doc = Jsoup.connect(link.attr("abs:href")).get();
                web = new Website(doc);
                //web.neighbors.add(parent1);
                web.neighborsNames.add(parent1.title);
                list1.add(web.title);
                if (!webList.contains(web)) {
                    webList.add(web);
                }
                //iterate through links on subpage and create website objs
                Elements linksOnPage2 = doc.select("#bodyContent a[href^=\"/wiki/\"]");
                int breakPoint2 = 0;
                parent2 = web;
                for (Element l : linksOnPage2) {
                    if (test(l)) {
                        breakPoint2++;
                        doc = Jsoup.connect(l.attr("abs:href")).get();
                        web = new Website(doc);
                        web.neighborsNames.add(parent2.title);
                        list2.add(web.title);
                        if (!webList.contains(web)) {
                            webList.add(web);
                        }
                    }
                    if (breakPoint2 == 12) {
                        break;
                    }
                }
                //add all the inner websites to the parent as neighbors
                parent2.neighborsNames.addAll(list2);
                list2.clear();
            }
            if (num == breakPoint) {
                break;
            }
        }
        //add all the inner websites to the parent as neighbors
        parent1.neighborsNames.addAll(list1);

        list1.clear();
        return webList;
    }

    static double getCost(Website w1, Website w2) {

        double similarity = 0;
        for (String s : w1.topWords) {
            //add both word frequencies to their frequncy lists
            int inputCount = w1.topWordsFreq.get(w1.topWords.indexOf(s));
            int testCount;
            if (w2.topWords.contains(s)) {
                testCount = w2.topWordsFreq.get(w2.topWords.indexOf(s));
            } else {
                //else add 1(can't use 0 because we dont want to divide by all 0's)
                testCount = 1;
            }
            w1.frequencyList.add(inputCount);
            w2.frequencyList.add(testCount);

        }
        similarity = cosineSimilarity(w1.frequencyList, w2.frequencyList);
        w1.frequencyList.clear();
        w2.frequencyList.clear();
        //return 1 - the similarity because the closer to 1 the more similar but we are trying to find least cost
        return 1 - similarity;
    }

    static double cosineSimilarity(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (int i = 0; i < list1.size() - 1; i++) {
            //for each word get the two frequencies
            //compute dot product and add to total
            dotProduct += (list1.get(i) * list2.get(i));
            //add the frequencies to their total
            magnitude1 += Math.pow(list1.get(i), 2);
            magnitude2 += Math.pow(list2.get(i), 2);
        }
        //take the sums and get the cosine simlilarity
        return dotProduct / ((Math.sqrt(magnitude1) * Math.sqrt(magnitude2)));
    }

}
