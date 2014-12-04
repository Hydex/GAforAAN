/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.AAN;

import ai6_aan.AAN.Node.Node;
import ai6_aan.AAN.Node.WorkerNode;
import ai6_aan.Utils.DataIndividual;
import ai6_aan.GA.Gene;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class Layer {

    private ArrayList<Node> nodes;

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public void setInputValue(DataIndividual indiv) {
        if (nodes.size() == indiv.getGene().length) {
            for (int i = 0; i != indiv.getGene().length; i++) {
                nodes.get(i).setOutputValue(indiv.getGene()[i]);
            }
        }
    }

    public void setWeights(ArrayList<Gene> genes) {
        for (int i = 0; i != nodes.size(); i++) {
            ((WorkerNode) nodes.get(i)).setWeights(genes.get(i));
        }
    }

    public double getOutputValue() {
        double outputValue = 0.0;
        for (Node node : nodes) {
            outputValue += node.getOutputValue();
        }

        return outputValue;
    }
}
