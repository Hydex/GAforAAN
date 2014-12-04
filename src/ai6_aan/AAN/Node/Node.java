/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.AAN.Node;

/**
 *
 * @author david
 */
public abstract class Node {
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public abstract double getOutputValue();
    public abstract void setOutputValue(double outputValue);
    public abstract void resetOutputValue();
    
    @Override
    public String toString(){
        return name; 
    }
}
