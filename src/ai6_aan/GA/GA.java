/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.GA;

import ai6_aan.AAN.AAN;
import static ai6_aan.AI6_AAN.trainingSet;
import ai6_aan.Utils.DataIndividual;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author david
 */
public class GA {

    private Random rand = new Random();
    private ArrayList<Individual> population;
    private AAN aan;
    private int competitorSize;
    private int crossoverPoint;
    private boolean randomCrossOver = true;
    private double mutationRate;
    private double crossoverRate;

    public GA(int inputLayerSize, int hiddenLayerSize, int outputLayerSize, int populationSize, AAN aan, int competitorSize, int crossoverPoint, boolean randomCrossOver, double mutationRate, double creepRate, double crossoverRate) {

        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.competitorSize = competitorSize;
        population = new ArrayList<>();

        if (!randomCrossOver) {
            this.crossoverPoint = crossoverPoint;
        } else {
            this.randomCrossOver = true;
        }

        for (int i = 0; i != populationSize; i++) {
            //population.add(IndividualFactory.newIndividual(inputLayerSize, hiddenLayerSize, outputLayerSize, rand, aan, creepRate));
            population.add(IndividualFactory.newIndividual(inputLayerSize, hiddenLayerSize, outputLayerSize, rand, aan, creepRate));
        }

    }

    public void doAlgorithm(int maxGenerations) {

        for (int i = 0; i != maxGenerations; i++) {

            if (i % 100 == 0) {
                getFitness(i);
            }
            doSelection();
            doCrossOver();
            doMutation();
        }
    }

    private void getFitness(int i) {
        DecimalFormat df = new DecimalFormat("0.000");
        System.out.print("Run:" + i + ":  ");
        for (Individual indiv : population) {
            System.out.print(df.format(indiv.getError()) + " ");
        }
        System.out.println();
    }

//    private void doSelection() {
//        ArrayList<Individual> newPopulation = new ArrayList<>();
//        for (int i = 0; i != (int) (population.size() / 2); i++) {
//            Individual[] competitors = new Individual[competitorSize];
//
//            for (int c = 0; c != competitorSize; c++) {
//                competitors[c] = population.get(rand.nextInt(population.size()));
//            }
//
//            newPopulation.add(getWinner(competitors).createClone());
//        }
//
//        while(newPopulation.size() != population.size()){
//            newPopulation.add(population.get(rand.nextInt(population.size())).createClone());
//        }
//        this.population = newPopulation;
//    }
//    
//    private Individual getWinner(Individual[] competitors) {
//        Individual bestIndiv = null;
//        double bestFitness = 0;
//
//        for (Individual indiv : competitors) {
//            double fitness = indiv.getError();
//            if ((bestFitness >= fitness) || (bestIndiv == null)) {
//                bestFitness = fitness;
//                bestIndiv = indiv;
//            }
//        }
//
//        return bestIndiv;
//    }
    private void doSelection() {

        //1. SELECT top 2
        //2. SELECT bottom 2
        //3. SELECT random stuff
        ArrayList<Individual> newPopulation = new ArrayList<>();

        int populationSize = population.size() / 3;

        ArrayList<Individual> winner = getWinnerAcrossPopulation(20);
        ArrayList<Individual> looser = getLooserAcrossPopulation(20);

        while (winner.size() > 0) {
            newPopulation.add(winner.get(0));
            winner.remove(0);
        }

        while (looser.size() > 0) {
            newPopulation.add(looser.get(0));
            looser.remove(0);
        }

        while (newPopulation.size() != population.size()) {
            newPopulation.add(population.get(rand.nextInt(population.size())));
        }

        this.population = newPopulation;
    }

    private ArrayList<Individual> tempPopulation() {
        ArrayList<Individual> temp = new ArrayList<>();
        for (Individual indiv : population) {
            temp.add(indiv.createClone());
        }
        return temp;
    }

    private ArrayList<Individual> getLooserAcrossPopulation(int number) {
        ArrayList<Individual> looser = new ArrayList<>();
        ArrayList<Individual> tempPopulation = tempPopulation();
        while (looser.size() != number) {
            Individual worstIndividual = null;
            double worstIndividualFitness = 0.0;

            for (Individual indiv : tempPopulation) {
                if ((indiv.getError() >= worstIndividualFitness) | (worstIndividual == null)) {
                    worstIndividual = indiv;
                    worstIndividualFitness = indiv.getError();
                }
            }

            looser.add(worstIndividual);
            tempPopulation.remove(worstIndividual);
        }

        return looser;
    }

    public ArrayList<Individual> getWinnerAcrossPopulation(int number) {
        ArrayList<Individual> winner = new ArrayList<>();
        ArrayList<Individual> tempPopulation = tempPopulation();

        while (winner.size() != number) {

            Individual bestIndividual = null;
            double bestIndividualFitness = 0.0;

            for (Individual indiv : tempPopulation) {
                if ((indiv.getError() <= bestIndividualFitness) | (bestIndividual == null)) {
                    bestIndividual = indiv;
                    bestIndividualFitness = indiv.getError();
                }
            }
            winner.add(bestIndividual);
            tempPopulation.remove(bestIndividual);

        }

        return winner;
    }

    private void doCrossOver() {
        ArrayList<Individual> newPopulation = new ArrayList<>();
        int i = 0;

        while (newPopulation.size() != population.size()) {
            if (rand.nextDouble() <= crossoverRate) {
                Individual parent1 = population.get(rand.nextInt(population.size()));
                Individual parent2 = population.get(rand.nextInt(population.size()));

                ArrayList<Individual> children = getChildren(parent1, parent2);

                while ((newPopulation.size() != population.size()) & (children.size() > 0)) {
                    newPopulation.add(children.get(0));
                    children.remove(0);
                }
            } else {
                newPopulation.add(population.get(rand.nextInt(population.size())));
            }
        }

        this.population = newPopulation;
    }

    private ArrayList<Individual> getChildren(Individual parent1, Individual parent2) {

        if (randomCrossOver) {
            crossoverPoint = rand.nextInt(parent1.geneLength());
        }

        ArrayList<Individual> children = parent1.doCrossOver(parent2, crossoverPoint);

        return children;
    }

    private void doMutation() {
        for (Individual indiv : population) {
            if (rand.nextDouble() <= mutationRate) {
                indiv.doMutation(rand.nextInt(indiv.geneLength()), rand.nextDouble());
                indiv.setHasChanged(true);
            }
        }
    }

    public ArrayList<Individual> getPopulation() {

        return population;
    }

}
