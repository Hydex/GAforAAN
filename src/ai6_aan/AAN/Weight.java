/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.AAN;

import ai6_aan.AAN.Node.Node;

/**
 *
 * @author david
 */
public class Weight {

    private Node inputNode;
    private Node outputNode;
    private double weight;

    public Weight() {

    }

    public Weight(Node inputNode, Node outputNode) {
        this.inputNode = inputNode;
        this.outputNode = outputNode;
    }

    public double getWeight() {
        return weight;
    }

    public synchronized void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getInputNode() {
        return inputNode;
    }

    public void setInputNode(Node inputNode) {
        this.inputNode = inputNode;
    }

    public Node getOutputNode() {
        return outputNode;
    }

    public void setOutputNode(Node outputNode) {
        this.outputNode = outputNode;
    }

    public double getOutput() {
        return inputNode.getOutputValue() * this.weight;
    }

}
