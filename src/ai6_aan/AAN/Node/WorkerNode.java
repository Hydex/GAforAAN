package ai6_aan.AAN.Node;

import ai6_aan.AAN.Weight;
import ai6_aan.GA.Gene;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class WorkerNode extends Node {

    private ArrayList<Weight> inputWeights;
    private Weight biasWeight;
    private double outputValue;

    public WorkerNode() {
        inputWeights = new ArrayList<>();
        biasWeight = new Weight();

        //Setup bias weight
        biasWeight.setInputNode(new BiasNode());
        biasWeight.setOutputNode(this);
    }

    public ArrayList<Weight> getInputWeights() {
        return inputWeights;
    }

    public void setInputWeights(ArrayList<Weight> inputWeights) {
        this.inputWeights = inputWeights;
    }

    @Override
    public double getOutputValue() {
        outputValue = 0.0; 
        for (Weight weight : inputWeights) {
            outputValue += weight.getOutput();
        }
        outputValue += biasWeight.getOutput();
        
        getSigmoidValue();
        
//        if(outputValue > 0.5){
//            return 1;
//        } else {
//            return 0;
//        }
        
         return outputValue;
    }

    @Override
    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    @Override
    public void resetOutputValue() {
        outputValue = 0.0;
    }

    public void setBiasWeightValue(double baisWeightValue) {
        this.biasWeight.setWeight(baisWeightValue);
    }

    public double getBiasWeightValue() {
        return this.biasWeight.getWeight();
    }

    public void setWeights(Gene gene) {
        int i = 0;
        for (Weight weight : inputWeights) {
            weight.setWeight(gene.getInputWeights()[i]);
            i++;
        }
        biasWeight.setWeight(gene.getBiasWeight());
    }

    private void getSigmoidValue() {
        outputValue = (1.0 / (1.0 + Math.pow(Math.E, (-1.0 * outputValue))));
    }

}
