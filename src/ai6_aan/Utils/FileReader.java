/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.Utils;

import static ai6_aan.AI6_AAN.trainingSet;
import static ai6_aan.AI6_AAN.unseenSet;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author david
 */
public class FileReader {

    private static File file;
    private static final int dataPart = 6;
    private static final int classificationPart = 7;

    public static DataIndividual[] readFile(String url) {
        file = new File(url);
        DataIndividual[] input = null;
        ArrayList<DataIndividual> tempInput = new ArrayList<>();

        try {
            Scanner scan = new Scanner(file);

            scan.nextLine();

            while (scan.hasNextLine()) {
                String str = scan.nextLine();

                String[] data = str.split(" ");
                
                double[] gene = new double[dataPart];
                for(int i = 0; i != dataPart; i++){
                    gene[i] = Double.parseDouble(data[i]);
                }
                int classification = Integer.parseInt(data[classificationPart - 1]);

                tempInput.add(new DataIndividual(gene, classification));
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

        return getIndividualArray(tempInput);

    }

   

    private static DataIndividual[] getIndividualArray(ArrayList<DataIndividual> tempInput) {
        DataIndividual[] input = new DataIndividual[tempInput.size()];

        for (int i = 0; i != tempInput.size(); i++) {
            input[i] = tempInput.get(i);
        }

        return input;
    }
    
    
    public static void generateDataSets(double percentSeen) {
        Random rand = new Random();
        String file = "/home/david/Dropbox/uni/year_4/AI/assignment/AI6_AAN/src/resource/data3.txt";
        DataIndividual[] rawData = FileReader.readFile(file);

        int numberSeen = (int) (rawData.length * percentSeen);

        ArrayList<DataIndividual> classification1 = new ArrayList<>();
        ArrayList<DataIndividual> classification0 = new ArrayList<>();

        for (DataIndividual indiv : rawData) {
            if (indiv.getClassification() == 1) {
                classification1.add(indiv);
            } else {
                classification0.add(indiv);
            }
        }

        ArrayList<DataIndividual> classification1_seenSet = new ArrayList<>();
        ArrayList<DataIndividual> classification0_seenSet = new ArrayList<>();

        int classification1Seen = (int) (classification1.size() * percentSeen);
        for (int i = 0; i != classification1Seen; i++) {
            int indiv = rand.nextInt(classification1.size());

            classification1_seenSet.add(classification1.get(indiv));
            classification1.remove(indiv);
        }

        int classification0Seen = (int) (classification0.size() * percentSeen);
        for (int i = 0; i != classification0Seen; i++) {
            int indiv = rand.nextInt(classification0.size());
            classification0_seenSet.add(classification0.get(indiv));
            classification0.remove(indiv);
        }

        System.out.println("Classification 0 Seen Set Size:" + classification0_seenSet.size() + "\t " + ((double) classification0_seenSet.size() / ((double) ((classification0_seenSet.size() + classification1_seenSet.size())) / 100.0)) + "%");
        System.out.println("Classification 1 Seen Set Size:" + classification1_seenSet.size() + "\t " + ((double) classification1_seenSet.size() / ((double) ((classification0_seenSet.size() + classification1_seenSet.size())) / 100.0)) + "%");

        trainingSet = new DataIndividual[numberSeen];

        for (int i = 0; i != trainingSet.length; i++) {
            if ((classification1_seenSet.size() > 0) & (classification0_seenSet.size() > 0)) {
                if (rand.nextDouble() > 0.5) {
                    int indiv = rand.nextInt(classification0_seenSet.size());
                    trainingSet[i] = classification0_seenSet.get(indiv);
                    classification0_seenSet.remove(indiv);
                } else {
                    int indiv = rand.nextInt(classification1_seenSet.size());
                    trainingSet[i] = classification1_seenSet.get(indiv);
                    classification1_seenSet.remove(indiv);
                }
            } else if (classification1_seenSet.size() > 0) {
                int indiv = rand.nextInt(classification1_seenSet.size());
                trainingSet[i] = classification1_seenSet.get(indiv);
                classification1_seenSet.remove(indiv);
            } else if (classification0_seenSet.size() > 0) {
                int indiv = rand.nextInt(classification0_seenSet.size());
                trainingSet[i] = classification0_seenSet.get(indiv);
                classification0_seenSet.remove(indiv);
            }
        }
//        trainingSet = new DataIndividual[772 + 772];
//        int c1 = 772;
//        int c0 = 772;
//        int ci = 0;
//        while (ci != trainingSet.length) {
//            if (rand.nextDouble() > 0.5) {
//                if ((classification0_seenSet.size() > 0) & (c0 > 0)) {
//                    trainingSet[ci] = classification0_seenSet.get(0);
//                    classification0_seenSet.remove(0);
//                    c0--;
//                    ci++;
//                }
//            } else {
//                if ((classification1_seenSet.size() > 0) & (c1 > 0)) {
//                    trainingSet[ci] = classification1_seenSet.get(0);
//                    classification1_seenSet.remove(0);
//                    c1--;
//                    ci++;
//                }
//            }
//        }

        System.out.println("Classification 0 Unseen Set Size:" + classification0.size() + "\t " + (double) (classification0.size() / ((double) (classification0.size() + classification1.size()) / 100.0)) + "%");
        System.out.println("Classification 1 Unseen Set Size:" + classification1.size() + "\t " + (double) (classification1.size() / ((double) (classification0.size() + classification1.size()) / 100)) + "%");

        unseenSet = new DataIndividual[rawData.length - numberSeen];
        for (int i = 0; i != unseenSet.length; i++) {
            if (classification1.size() > 0) {
                unseenSet[i] = classification1.get(0);
                classification1.remove(0);
            } else {
                unseenSet[i] = classification0.get(0);
                classification0.remove(0);
            }
        }

    }
}
