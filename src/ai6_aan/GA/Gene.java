/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.GA;

/**
 *
 * @author david
 */
public class Gene {

    private double[] inputWeights;
    private double biasWeight;

    public Gene(int inputLayerSize) {
        inputWeights = new double[inputLayerSize];

        for (int i = 0; i != inputWeights.length; i++) {
            inputWeights[i] = 0.0;
        }

        biasWeight = 0.0;
    }

    public void setInputWeights(double[] inputWeights) {
        this.inputWeights = inputWeights;
    }

    public void setBiasWeight(double biasWeight) {
        this.biasWeight = biasWeight;
    }

    public double[] getInputWeights() {
        return inputWeights;
    }

    public double getBiasWeight() {
        return biasWeight;
    }

    public Gene createClone() {
        Gene cloneGene = new Gene(inputWeights.length);

        double[] cloneInputWeights = new double[inputWeights.length];

        for (int i = 0; i != inputWeights.length; i++) {
            cloneInputWeights[i] = inputWeights[i];
        }

        cloneGene.setInputWeights(cloneInputWeights);
        cloneGene.setBiasWeight(biasWeight);

        return cloneGene;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (double weight : inputWeights) {
            sb.append(weight);
        }

        sb.append(biasWeight);

        return sb.toString();
    }
}
