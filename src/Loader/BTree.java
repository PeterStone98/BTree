/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.io.*;
import java.util.Arrays;

/**
 *
 * @author Pete
 */
public class BTree implements Serializable {

    static int T = 5;
    int maxFrequency = 1;//max word refquency
    Node root; //root Node 
    int topWordsSize = 30;
    int[] topFreq = new int[topWordsSize];
    Word[] topWords = new Word[topWordsSize];

    //node class
    static class Node implements Serializable {

        int numKeys = 0;//number of keys
        Node[] children = new Node[(2 * T)];//array of children
        Word[] keys = new Word[(2 * T - 1)];//array of keys
        Boolean isLeaf = null;//boolean leaf value

        // create new node
        private Node() {
        }
    }

    public BTree() throws IOException, ClassNotFoundException {
        //fill arrays to prevent null pointer
        Arrays.fill(topFreq, 0);
        Word w = new Word("", 0);
        Arrays.fill(topWords, w);
        //create btree root
        Node X = new Node();//create new node
        X.isLeaf = true;//assign leaf value to true
        Word key = new Word("null", 1);//first word to be added
        X.keys[0] = key;
        X.numKeys++;
        root = X;
    }

    Word search(String key) {
        return this.search(root, key);
    }

    void print() {
        this.printBtree(root, "");
    }

    void add(String s) throws IOException, ClassNotFoundException {
        Word w = this.search(s);
        if (w != null) {
            w.frequency++;
            boolean alreadyIn = false;
            for (int i = 0; i < topWordsSize; i++) {
                if (s.equals(topWords[i].name)) {
                    topFreq[i]++;
                    alreadyIn = true;
                }
            }
            if (!alreadyIn) {
                for (int i = 0; i < topWordsSize; i++) {
                    if (w.frequency > topFreq[i]) {
                        topFreq[i] = w.frequency;
                        topWords[i] = w;
                        break;
                    }
                }
            }
        } else {
            Word newWord = new Word(s, 1);
            this.insert(newWord);
        }
    }

    void insert(Word key) throws IOException, ClassNotFoundException {
        //start at root
        Node rootNode = root;
        //if root is full(means the whole tree is full)
        if (rootNode.numKeys == (2 * T - 1)) {
            // Create a new node
            Node newRoot = new Node();
            //this will be the new root
            root = newRoot;
            //the new root is no longer a leaf(if first split)
            newRoot.isLeaf = false;
            //add old root as child
            newRoot.children[0] = rootNode;
            //split the previous root(rootNode)
            splitChild(newRoot, 1, rootNode);
            //insert the key
            insertNonfull(root, key);
        } else {
            //insert key into tree
            insertNonfull(rootNode, key);
        }
    }

    void insertNonfull(Node node, Word key) throws IOException, ClassNotFoundException {
        //get number of keys in node
        int i = node.numKeys;
        //if leaf then insert key 
        if (node.isLeaf) {
            //check key value to determine where to insert
            while (i >= 1 && key.name.compareTo(node.keys[i - 1].name) > 0) {
                //switch key positions to make way for new key
                node.keys[i] = node.keys[i - 1];
                i--;
            }
            //insert key into correct postion
            node.keys[i] = key;
            //increase numKeys
            node.numKeys++;
        } else {
            //not leaf(get child)    
            while (i >= 1 && key.name.compareTo(node.keys[i - 1].name) > 0) {
                //get correct child to insert key
                i--;
            }
            //if node is full then split
            if (node.children[i].numKeys == (2 * T - 1)) {
                //split child
                splitChild(node, i + 1, node.children[i]);
                //get correct child to insert to
                if (key.name.compareTo(node.keys[i].name) < 0) {
                    i++;
                }
            }
            //else insert into the child
            insertNonfull(node.children[i], key);
        }
    }

    void splitChild(Node X, int index, Node Y) throws IOException, ClassNotFoundException {
        //create new Node      |      Y
        //         X           |    / 
        //        / \          |  X
        //      Y     Z        |    \
        //Y is full node       |      Z
        //Z is new brother node
        //X is parent node
        //X.children[index-1]=Y
        Node Z = new Node();
        Z.isLeaf = Y.isLeaf;
        Z.numKeys = T - 1;
        //insert largest keys to new node 
        for (int j = 0; j < T - 1; j++) {
            Z.keys[j] = Y.keys[j + T];
        }

        //if Y is not a leaf then assign largest children to Z
        if (!Y.isLeaf) {
            for (int j = 0; j < T; j++) {
                Z.children[j] = Y.children[j + T];
            }
        }

        Y.numKeys = T - 1;
        //switch X children to make room for new one
        for (int j = X.numKeys + 1; j >= index; j--) {
            X.children[j] = X.children[j - 1];
        }

        //assign the child reference for new node
        X.children[index] = Z;

        //switch parent keys to make room for new one
        for (int j = X.numKeys; j >= index; j--) {
            X.keys[j] = X.keys[j - 1];
        }

        //assign the key reference for new key
        X.keys[index - 1] = Y.keys[T - 1];
        X.numKeys++;
    }

    Word search(Node node, String key) {
        int i = 1;
        //serch for correct spot
        while (i <= node.numKeys && key.compareTo(node.keys[i - 1].name) < 0) {
            i++;
        }
        //check if the key is equal
        if (i <= node.numKeys && key.equals(node.keys[i - 1].name)) {
            //return node
            return node.keys[i - 1];
        }
        // if not leaf move to child
        if (!node.isLeaf) {
            //search subtree of node
            return search(node.children[i - 1], key);
        }
        // return false if not found
        return null;
    }

    public int getFreq(String s) {
        Word w = this.search(s);
        if (w != null) {
            return w.frequency;
        }
        //return 1 to prevent all 0's in a comparison
        return 1;
    }

    void printBtree(Node node, String indent) {
        // if the root is null print
        if (node == null) {
            System.out.println("The B-Tree is Empty");
        } else {
            System.out.println(indent + " ");
            String childIndent = indent + "\t";

            //print value then child values
            for (int i = 0; i < node.numKeys; i++) {
                if (!node.isLeaf) {
                    printBtree(node.children[i], childIndent);
                }
                // print the keys
                System.out.println(childIndent + node.keys[i]);
            }
            if (!node.isLeaf) {
                printBtree(node.children[node.numKeys], childIndent);
            }
        }
    }
}
