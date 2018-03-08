package com.jason.snake;


/**
 * Created by JasonWong on 07-Nov-17.
 */
public class GeneticAlgo {

    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.03;
    private static final int tournamentSize = 30;
    private static final boolean elitism = true;

    static int inputSize = 6;
    static int hiddenSize = 8;
    static int outputSize = 3;

    int popSize = 50;

    int currentPlayer = 0;
    int generation = 1;

    Population population = new Population(popSize,true);


    public Population evolvePopulation(Population pop) {
        Population newPopulation =  new Population(pop.players.length,false);
        // Keep our best individual
        if(elitism){
            newPopulation.players[0] = pop.getFittest();
        }
        int elitismOffset;

        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        //cross over
        for (int i = elitismOffset; i < pop.players.length; i++) {
            Player indiv1 = tournamentSelection(pop);
            Player indiv2 = tournamentSelection(pop);
            Player newIndiv = crossover(indiv1, indiv2);
            newPopulation.players[i] = newIndiv;
        }

        for (int i = elitismOffset; i < newPopulation.players.length; i++) {
            mutate(newPopulation.players[i]);
        }
        newPopulation.players[0].fitness = 0;
        return newPopulation;
    }

    private Player tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.players.length);
            tournament.players[i] = pop.players[randomId];
        }
        // Get the fittest
        Player fittest = tournament.getFittest();
        return fittest;
    }

    private Player crossover(Player p1, Player p2) {
        Player newSol = new Player();
        // Loop through genes

        for (int i = 0; i < inputSize + 1; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                if (Math.random() <= uniformRate) {
                    newSol.weightIH[i][j] =p1.weightIH[i][j];
                } else {
                    newSol.weightIH[i][j] =p2.weightIH[i][j];
                }
            }
        }
        for (int i = 0; i < hiddenSize + 1; i++) {
            for (int j = 0; j < outputSize; j++) {
                if (Math.random() <= uniformRate) {
                    newSol.weightHO[i][j] =p1.weightHO[i][j];
                } else {
                    newSol.weightHO[i][j] =p2.weightHO[i][j];
                }
            }
        }
        return newSol;
    }

    private void mutate(Player p1) {
        // Loop through genes

        for (int i = 0; i < inputSize + 1; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                if (Math.random() <= mutationRate) {
                    p1.weightIH[i][j] = (Math.random()-0.5)*2;
                }
            }
        }
        for (int i = 0; i < hiddenSize + 1; i++) {
            for (int j = 0; j < outputSize; j++) {
                if (Math.random() <= mutationRate) {
                    p1.weightHO[i][j] = (Math.random()-0.5)*2;
                }
            }
        }
    }
}

class Population {
    Player[] players;

    Population (int size,boolean init){
        players = new Player[size];
        if(init){
            for(int i = 0;i<players.length;i++){
                Player p = new Player();
                players[i] = p;
            }

        }

    }
    public Player getFittest() {
        Player fittest = players[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < players.length; i++) {
            if (fittest.fitness <= players[i].fitness) {
                fittest = players[i];
            }
        }
        return fittest;
    }
}

class Player {
    static int inputSize = 6;
    static int hiddenSize = 8;
    static int outputSize = 3;

    //Input to Hidden weights
    double[][] weightIH = new double[inputSize+1][hiddenSize];
    //Hidden to Output weights
    double[][] weightHO = new double[hiddenSize+1][outputSize];

    //Neurons
    double[] inputNeurons = new double[inputSize];
    double[] hiddenNeurons = new double[hiddenSize];
    double[] outputNeurons = new double[outputSize];


    int fitness;

    public Player(){
        for (int i = 0; i < inputSize +1; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightIH[i][j] = (Math.random() - 0.5)*2;
            }
        }
        for (int i = 0; i < hiddenSize+1; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightHO[i][j] = (Math.random() - 0.5)*2;
            }
        }
    }

    public void compute(){
        double sum;
        for (int i = 0; i < hiddenSize; i++) {
            sum = 0;
            for (int j = 0; j < inputSize; j++) {
                sum += inputNeurons[j] * weightIH[j][i];
            }
            //add bias
            //sum += weightIH[inputSize][i];
            hiddenNeurons[i] = sigmoid(sum);
        }

        for (int i = 0; i < outputSize; i++) {
            sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenNeurons[j] * weightHO[j][i];
            }

            //sum+= weightHO[hiddenSize][i];
            outputNeurons[i] = sigmoid(sum);
        }
    }

    static double sigmoid(double x){
        return 1/(1+Math.exp(-x));
    }

}

