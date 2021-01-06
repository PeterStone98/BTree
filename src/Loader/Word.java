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
public class Word implements Serializable {

    public String name;
    public int frequency;

    public Word(String s, int f) {
        this.name = s;
        this.frequency = f;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.frequency;
    }
}
