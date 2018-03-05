package com.jason.snake;

public class BackProp {
    int inputSize = 6;
    int hiddenSize = 14;
    int outputSize = 3;

    //Input to Hidden weights
    double[][] weightIH = new double[inputSize + 1][hiddenSize];
    //Hidden to Output weights
    double[][] weightHO = new double[hiddenSize + 1][outputSize];

    //Neurons
    double[] inputNeurons = new double[inputSize];
    double[] hiddenNeurons = new double[hiddenSize];
    double[] outputNeurons = new double[outputSize];
    double[] dataNeurons = new double[outputSize];

    //Errors
    double errOut[] = new double[outputSize];
    double errHidden[] = new double[hiddenSize];

    double learnRate = 0.1;

    public BackProp() {
        for (int i = 0; i < inputSize + 1; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightIH[i][j] = (Math.random() - 0.5)*2;
            }
        }
        for (int i = 0; i < hiddenSize + 1; i++) {
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
            sum += weightIH[inputSize][i];
            hiddenNeurons[i] = sigmoid(sum);
        }

        for (int i = 0; i < outputSize; i++) {
            sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenNeurons[j] * weightHO[j][i];
            }

            sum+= weightHO[hiddenSize][i];
            outputNeurons[i] = sigmoid(sum);
        }
    }

    public void backPropagate(){
        //output layer error
        for (int i = 0; i < outputSize; i++) {
            errOut[i] = (dataNeurons[i] - outputNeurons[i] ) * sigmoidDerivative(outputNeurons[i]);
        }
        //hidden layer error
        for (int i = 0; i < hiddenSize; i++) {
            errHidden[i] = 0;
            for (int j = 0; j < outputSize; j++) {
                errHidden[i] += errOut[j] * weightHO[i][j];
            }
            errHidden[i] *= sigmoidDerivative(hiddenNeurons[i]);
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightHO[j][i] += (learnRate * errOut[i] * hiddenNeurons[j]);
            }
            weightHO[hiddenSize][i] += (learnRate * errOut[i]);
        }

        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weightIH[j][i] += (learnRate * errHidden[i] * inputNeurons[j]);
            }
            weightIH[inputSize][i] += (learnRate * errHidden[i]);
        }


    }
    static double sigmoid(double x){
        return (1/(1+Math.exp(-x)));
    }

    static double sigmoidDerivative(double val)
    {
        return (val * (1.0 - val));
    }
}
