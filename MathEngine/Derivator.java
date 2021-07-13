package com.fermi.MathEngine;

import com.fermi.MathEngine.LinearAlgebra.Algebra.Function;

import java.math.BigDecimal;

public class Derivator {

    //----------------------------------------------------------------------------------------------------

    //Variáveis:

    private Function function;

    //----------------------------------------------------------------------------------------------------

    //Construtores:

    public Derivator(Function function){
        this.function = function;
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos Públicos:

    public double firstDerivative(String[] variables, double[] position, String direction){

        //Criando um novo endereço de memória para o vetor dos valores:
        position = position.clone();

        int directionIndex = 0;
        for (int i=0; i < variables.length; i++)
            if(variables[i].equals(direction))
                directionIndex = i;

        double increment = Math.pow(10,-4);

        position[directionIndex] = position[directionIndex] + increment;
        double fa = function.calculate(variables, position);

        position[directionIndex] = position[directionIndex] - 2*increment;
        double fb = function.calculate(variables, position);

        return  new BigDecimal((fa-fb)/(2*increment)).doubleValue();
    }

    public double[] gradient(String[] variables, double[] position){

        double[] gradientVector = new double[variables.length];

        for(int index = 0; index < variables.length; index++){
            gradientVector[index] = firstDerivative(variables, position, variables[index]);
        }

        return gradientVector;
    }

    public double secondDerivative(String[] variables, double[] position, String direction){

        //Criando um novo endereço de memória para o vetor dos valores:
        position = position.clone();

        int directionIndex = 0;
        for (int i=0; i < variables.length; i++)
            if(variables[i].equals(direction))
                directionIndex = i;

        double increment = Math.pow(10,-4);

        position[directionIndex] = position[directionIndex] + increment;
        double fa = firstDerivative(variables, position, direction);

        position[directionIndex] = position[directionIndex] - 2*increment;
        double fb = firstDerivative(variables, position, direction);

        return new BigDecimal((fa-fb)/(2*increment)).doubleValue();
    }

    public double mixedDerivative(String[] variables, double[] position, String[] directions){

        //Criando um novo endereço de memória para o vetor dos valores:
        position = position.clone();

        int[] directionIndexes = new int[]{0,0};

        for (int i=0; i < variables.length; i++){
            if(variables[i].equals(directions[0]))
                directionIndexes[0] = i;

            if(variables[i].equals(directions[1]))
                directionIndexes[1] = i;
        }

        double increment = Math.pow(10,-4);

        position[directionIndexes[1]] = position[directionIndexes[1]] + increment;
        double fa = firstDerivative(variables,position,variables[directionIndexes[0]]);

        position[directionIndexes[1]] = position[directionIndexes[1]] - 2*increment;
        double fb = firstDerivative(variables,position,variables[directionIndexes[0]]);

        return  new BigDecimal((fa-fb)/(2*increment)).doubleValue();
    }

    public double[][] hessian(String[] variables, double[] position){

        double[][] hessian = new double[variables.length][variables.length];
        String[] directions = new String[2];

        for(int i = 0; i < variables.length; i++){
            for(int j = 0; j < variables.length; j++){
                directions[0] = variables[i];
                directions[1] = variables[j];

                //System.out.println(String.format("Derivada....: %s.%s", directions[0],directions[1]));
                hessian[i][j] = mixedDerivative(variables,position,directions);
            }
        }

        return hessian;
    }

    //----------------------------------------------------------------------------------------------------
}
