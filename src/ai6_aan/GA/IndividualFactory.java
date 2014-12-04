/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.GA;

import ai6_aan.AAN.AAN;
import java.util.Random;

/**
 *
 * @author david
 */
public abstract class IndividualFactory {

    public static Individual newIndividual(int inputLayerSize, int hiddenLayerSize, int outputLayerSize, Random rand, AAN aan, double creepRate) {

        //   Individual indiv = new Individual(inputLayerSize, hiddenLayerSize, outputLayerSize, aan, creepRate);
        Individual indiv = new Individual(inputLayerSize, hiddenLayerSize, outputLayerSize, aan, creepRate);

        for (Gene hiddenGene : indiv.getHiddenLayerGenes()) {

            hiddenGene.setInputWeights(setInputWeights(hiddenGene, rand));
            hiddenGene.setBiasWeight(rand.nextDouble());
        }

        for (Gene outputGene : indiv.getOutputLayerGenes()) {

            outputGene.setInputWeights(setInputWeights(outputGene, rand));
            outputGene.setBiasWeight(rand.nextDouble());
        }

        return indiv;
    }

    private static double[] setInputWeights(Gene indiv, Random rand) {
        double[] weights = new double[indiv.getInputWeights().length];

        for (int i = 0; i != weights.length; i++) {
            double weight = rand.nextDouble();

            if (rand.nextDouble() > rand.nextDouble()) {
                weights[i] = weight * -1;
            } else {
                weights[i] = weight;
            }
        }

        return weights;
    }
}
