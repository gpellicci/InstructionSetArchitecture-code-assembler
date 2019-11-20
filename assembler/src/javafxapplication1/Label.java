/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

/**
 *
 * @author nicom
 */
public class Label {
    private String name;
    private int line;
    
    public Label(String n, int l) {
        name = n;
        line = l;
    }
    
    public String getName() {
        return name;
    }
    
    public int getLine() {
        return line;
    }
}
