/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan;

import ai6_aan.AAN.AAN;
import ai6_aan.AAN.Layer;
import ai6_aan.AAN.Weight;
import ai6_aan.AAN.Node.InputNode;
import ai6_aan.AAN.Node.Node;
import ai6_aan.AAN.Node.WorkerNode;
import ai6_aan.GA.GA;
import ai6_aan.Utils.DataIndividual;
import ai6_aan.GA.Individual;
import ai6_aan.GA.IndividualFactory;
import ai6_aan.Utils.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author david
 */
public class AI6_AAN {

    private static int inputLayerSize;
    private static int hiddenLayerSize;
    private static int outputLayerSize;
    private static double percentSeen = 0.8;
    public static DataIndividual[] trainingSet;
    public static DataIndividual[] unseenSet;

    public static void main(String[] args) {

        FileReader.generateDataSets(percentSeen);

        inputLayerSize = trainingSet[0].getGene().length;
        hiddenLayerSize = 10;
        outputLayerSize = 1;

        int populationSize = 1000;
        int competitorSize = 2;
        int crossoverPoint = 6;
        boolean randomCrossOver = true;
        double creepRate = 0.005;
        double mutationRate = 0.7;
        double crossoverRate = 0.03;
        AAN aan = new AAN(inputLayerSize, hiddenLayerSize, outputLayerSize);
        GA ga = new GA(inputLayerSize, hiddenLayerSize, outputLayerSize, populationSize, aan, competitorSize, crossoverPoint, randomCrossOver, mutationRate, creepRate, crossoverRate);

        ga.doAlgorithm(1500000);

        ArrayList<Individual> bestIndivs = ga.getWinnerAcrossPopulation(5);

        for (Individual indiv : bestIndivs) {
            int correctness = indiv.getCorrect(unseenSet);

            System.out.println((double) correctness / (double) (unseenSet.length / 100.0));
        }
    }

}
