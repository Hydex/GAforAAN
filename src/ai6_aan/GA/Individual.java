/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.GA;

import ai6_aan.AAN.AAN;
import static ai6_aan.AI6_AAN.trainingSet;
import ai6_aan.Utils.DataIndividual;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class Individual {

    private int hiddenLayerInputSize;
    private int hiddenLayerSize;
    private int outputLayerSize;
    private double fitness = -1;
    private ArrayList<Gene> hiddenLayerGenes;
    private ArrayList<Gene> outputLayerGenes;
    private AAN aan;
    private boolean hasChanged;
    private static double creepRate;

    public Individual(int hiddenLayerInputSize, int hiddenLayerSize, int outputLayerSize, AAN aan, double creepRate) {
        this.hiddenLayerInputSize = hiddenLayerInputSize;
        this.hiddenLayerSize = hiddenLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.creepRate = creepRate;
        hiddenLayerGenes = new ArrayList<>();
        outputLayerGenes = new ArrayList<>();

        this.aan = aan;
        hasChanged = true;

        for (int i = 0; i != hiddenLayerSize; i++) {
            hiddenLayerGenes.add(new Gene(hiddenLayerInputSize));
        }

        for (int i = 0; i != outputLayerSize; i++) {
            outputLayerGenes.add(new Gene(hiddenLayerSize));
        }
    }

    public ArrayList<Individual> doCrossOver(Individual parent2, int crossoverPoint) {

        ArrayList<Individual> children = new ArrayList<>();
        Individual child1 = this.createClone();
        Individual child2 = parent2.createClone();

        double[] parent1Gene = this.getGenes();
        double[] parent2Gene = parent2.getGenes();
        double[] child1Gene = new double[geneLength()];
        double[] child2Gene = new double[geneLength()];

        for (int i = 0; i != crossoverPoint; i++) {
            child1Gene[i] = parent1Gene[i];
            child2Gene[i] = parent2Gene[i];
        }

        for (int i = crossoverPoint; i != parent1Gene.length; i++) {
            child2Gene[i] = parent1Gene[i];
            child1Gene[i] = parent2Gene[i];
        }

        child1.setGenes(child1Gene);
        child2.setGenes(child2Gene);
        child1.setHasChanged(true);
        child2.setHasChanged(true);
        children.add(child1);
        children.add(child2);
        //    children.add(this.createClone());
        //    children.add(parent2.createClone());

        return children;
    }

    private int hiddenLayerGeneLength() {
        int geneLength = 0;
        for (Gene gene : hiddenLayerGenes) {
            geneLength += gene.getInputWeights().length;
            geneLength += 1; //hidden layer bias weight
        }
        return geneLength;
    }

    private int outputLayerGeneLength() {
        int geneLength = 0;

        for (Gene gene : outputLayerGenes) {
            geneLength += gene.getInputWeights().length;
            geneLength += 1; //output layer gene bias weight
        }

        return geneLength;
    }

    public int geneLength() {
        int geneLength = 0;

        geneLength += hiddenLayerGeneLength();
        geneLength += outputLayerGeneLength();

        return geneLength;
    }

    public void setGenes(double[] genes) {
        int i = 0;
        i = encodeGene(hiddenLayerGenes, i, genes);
        encodeGene(outputLayerGenes, i, genes);
        hasChanged = true;
    }

    public double[] getGenes() {
        double[] genes = new double[geneLength()];
        int i = 0;

        i = decodeGene(hiddenLayerGenes, i, genes);
        decodeGene(outputLayerGenes, i, genes);

        return genes;
    }

    private int encodeGene(ArrayList<Gene> layer, int counter, double[] genes) {
        for (Gene gene : layer) {
            double[] newGene = gene.getInputWeights();
            for (int i = 0; i != gene.getInputWeights().length; i++) {
                newGene[i] = genes[counter];
                counter++;
            }
            gene.setInputWeights(newGene);
            gene.setBiasWeight(genes[counter]);
            counter++;
        }

        return counter;
    }

    private int decodeGene(ArrayList<Gene> layer, int counter, double[] genes) {

        for (Gene gene : layer) {
            for (int i = 0; i != gene.getInputWeights().length; i++) {
                genes[counter] = gene.getInputWeights()[i];
                counter++;
            }
            genes[counter] = gene.getBiasWeight();
            counter++;
        }

        return counter;
    }

    public ArrayList<Gene> getHiddenLayerGenes() {
        return hiddenLayerGenes;
    }

    public void setHiddenLayerGenes(ArrayList<Gene> hiddenLayerWeights) {
        this.hiddenLayerGenes = hiddenLayerWeights;
        hasChanged = true;
    }

    public ArrayList<Gene> getOutputLayerGenes() {
        return outputLayerGenes;
    }

    public void setOutputLayerGenes(ArrayList<Gene> outputLayerWeights) {
        this.outputLayerGenes = outputLayerWeights;
        hasChanged = true;
    }

    public double getError() {
        if (hasChanged) {
            fitness = 0;
            aan.setWeights(this);

            for (DataIndividual data : trainingSet) {
                aan.setInputValues(data);

                double output = aan.getOutput();

                fitness += Math.abs((double) data.getClassification() - output);

            }
        }
        return fitness;
    }

    public int getCorrect(DataIndividual[] data) {
        int correctness = 0;
        aan.setWeights(this);

        for (DataIndividual dataPoint : data) {
            aan.setInputValues(dataPoint);

            int classification = 0;
            if (aan.getOutput() > 0.9) {
                classification = 1;
            }
            if (classification == dataPoint.getClassification()) {
                correctness++;
            }

        }

        return correctness;
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Individual createClone() {
        Individual cloneIndividual = new Individual(hiddenLayerInputSize, hiddenLayerSize, outputLayerSize, aan, creepRate);

        ArrayList<Gene> cloneHiddenLayer = new ArrayList<>();
        for (Gene hiddenLayerGene : hiddenLayerGenes) {
            cloneHiddenLayer.add(hiddenLayerGene.createClone());
        }

        ArrayList<Gene> cloneOutputLayer = new ArrayList<>();
        for (Gene outputLayer : outputLayerGenes) {
            cloneOutputLayer.add(outputLayer.createClone());
        }

        cloneIndividual.setHiddenLayerGenes(cloneHiddenLayer);
        cloneIndividual.setOutputLayerGenes(cloneOutputLayer);
        cloneIndividual.setFitness(fitness);
        cloneIndividual.setHasChanged(false);
        return cloneIndividual;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Gene gene : hiddenLayerGenes) {
            sb.append(gene.toString());
            sb.append("\t");
        }
        for (Gene gene : outputLayerGenes) {
            sb.append(gene.toString());
            sb.append("\t");
        }

        return sb.toString();
    }

    public void doMutation(int mutantGene, double creepChange) {
        if (creepChange > 0.5) {
            increaseCreep(mutantGene);
        } else {
            decreaseCreep(mutantGene);
        }
        hasChanged = true;
    }

    private void increaseCreep(int mutant) {
        double[] mutantGene = this.getGenes();
        mutantGene[mutant] = (mutantGene[mutant] + creepRate);

        //getBest(mutantGene);
        this.setGenes(mutantGene);
    }

    private void decreaseCreep(int mutant) {
        double[] mutantGene = this.getGenes();
        mutantGene[mutant] = (mutantGene[mutant] - creepRate);
        //getBest(mutantGene);
        this.setGenes(mutantGene);

    }

    private void getBest(double[] mutantGene) {
        Individual mutantIndiv = this.createClone();
        mutantIndiv.setGenes(mutantGene);
        mutantIndiv.setHasChanged(true);

        if (mutantIndiv.getError() >= this.getError()) {
            this.setGenes(mutantGene);
        }
    }

}
