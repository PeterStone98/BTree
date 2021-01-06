/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Pete
 */
public class KeyClass implements Serializable {
    int value;//frequency
    public ArrayList<String> wordList = new ArrayList<>();//words with this frequency
    BTree.Node node;
    public KeyClass(){}
    
    public KeyClass(int v){
        value = v;
    }
    
    @Override
    public String toString(){
        return "|"+value+"|"+wordList;
    }
}
