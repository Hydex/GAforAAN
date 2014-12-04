/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.AAN;

import ai6_aan.AAN.Node.InputNode;
import ai6_aan.AAN.Node.Node;
import ai6_aan.AAN.Node.WorkerNode;
import ai6_aan.Utils.DataIndividual;
import ai6_aan.GA.Individual;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class AAN {

    private Layer inputLayer;
    private Layer hiddenLayer;
    private Layer outputLayer;

    public AAN(int inputLayerSize, int hiddenLayerSize, int outputLayerSize) {
        initInputLayer(inputLayerSize);
        initHiddenLayer(hiddenLayerSize);
        initOutputLayer(outputLayerSize);
    }

    private void initInputLayer(int inputLayerSize) {
        inputLayer = new Layer();
        ArrayList<Node> inputArray = new ArrayList<>();
        for (int i = 0; i != inputLayerSize; i++) {
            InputNode node = new InputNode();
            node.setName("Input Node " + i);
            inputArray.add(node);
        }
        inputLayer.setNodes(inputArray);
    }

    private void initHiddenLayer(int hiddenLayerSize) {
        hiddenLayer = new Layer();

        ArrayList<Node> hiddenArray = new ArrayList<>();
        for (int i = 0; i != hiddenLayerSize; i++) {
            WorkerNode node = new WorkerNode();
            node.setName("Hidden Layer Node " + i);
            hiddenArray.add(node);
        }

        for (Node hiddenNode : hiddenArray) {
            ArrayList<Weight> inputWeight = new ArrayList<>();
            for (Node inputNode : inputLayer.getNodes()) {
                inputWeight.add(new Weight(inputNode, hiddenNode));
            }
            ((WorkerNode) hiddenNode).setInputWeights(inputWeight);
        }

        hiddenLayer.setNodes(hiddenArray);
    }

       private void initOutputLayer(int outputLayerSize) {
        outputLayer = new Layer();
        ArrayList<Node> outputArray = new ArrayList<>();
        for (int i = 0; i != outputLayerSize; i++) {
            WorkerNode node = new WorkerNode();
            node.setName("Output Layer Node " + i);
            outputArray.add(node);
        }

        for (Node outputNode : outputArray) {
            ArrayList<Weight> inputWeight = new ArrayList<>();
            for (Node hiddenNode : hiddenLayer.getNodes()) {
                inputWeight.add(new Weight(hiddenNode, outputNode));
            }

            ((WorkerNode) outputNode).setInputWeights(inputWeight);
        }

        outputLayer.setNodes(outputArray);
    }

    public void setInputValues(DataIndividual input) {
        inputLayer.setInputValue(input);
    }

    public void setWeights(Individual indiv) {
        hiddenLayer.setWeights(indiv.getHiddenLayerGenes());
        outputLayer.setWeights(indiv.getOutputLayerGenes());
    }

    public double getOutput() {
        return outputLayer.getOutputValue();
    }
}
