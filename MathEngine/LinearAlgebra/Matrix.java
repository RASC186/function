package com.fermi.MathEngine.LinearAlgebra;

/*
    Classe responsável por armazenar valores algébricos/numéricos na forma de uma matriz bidimencional.
*/

import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.VariableCollector;

public class Matrix {

    //----------------------------------------------------------------------------------------------------

    //Variáveis:

    private Element[][] elements;
    private String[] variables;
    private String id, title, matrix;

    //----------------------------------------------------------------------------------------------------

    //Construtores:

    public Matrix(){
        //Construtor vazio...
    }

    public Matrix (String matrixDeclaration){
        setMatrix(matrixDeclaration);
    }

    public Matrix(String id, double[][] matrix){
        setMatrix(id, matrix);
    }

    public Matrix(String[] variables, String id, String matrix){
        setMatrix(variables, id, matrix);
    }

    //----------------------------------------------------------------------------------------------------

    //Getters:

    @Override
    public String toString(){
        return title + " = " + matrix;
    }

    //----------------------------------------------------------------------------------------------------

    //Setters públicos:

    public void setMatrix(String matrixDeclaration) {

        //Isolando o título da matriz e a matriz em String's diferentes:

        matrixDeclaration = matrixDeclaration.replaceAll(" ", "");

        String title = matrixDeclaration.substring(
                0, matrixDeclaration.indexOf('=')
        );

        String expression = matrixDeclaration.substring(
                matrixDeclaration.indexOf('=')+1
        );

        //Criando o coletor de variáveis para gerar o vetor das variáveis e o identificador da matriz:

        VariableCollector variableCollector = new VariableCollector(title);

        //Armazenando o vetor de variáveis, a string da matriz e o seu identificador:

        this.id = variableCollector.getFunctionId();

        this.variables = new String[variableCollector.getVariables().size()];
        this.variables = variableCollector.getVariables().toArray(variables);

        this.title = title;
        this.matrix = expression;

        //Armazenando os elementos da matrix:

        setElements(this.variables, this.id, this.matrix);
    }

    public void setMatrix(String id, double[][] matrix){

        //Armazenando o vetor de variáveis, a string da matriz e o seu identificador:

        this.id = id;
        this.variables = null;
        this.title = id;
        this.matrix = matrix2String(matrix);

        //Armazenando os elementos da matrix:

        setElements(this.variables, this.id, this.matrix);
    }

    public void setMatrix(String[] variables, String id, String matrix) {

        //Criando o coletor de variáveis para gerar o título da matriz:

        VariableCollector variableCollector = new VariableCollector(variables, id);

        //Armazenando o vetor de variáveis, a string da matriz e o seu identificador:

        this.id = id;
        this.variables = variables;
        this.title = variableCollector.getFunctionTitle();
        this.matrix = matrix.replaceAll(" ", "");

        //Armazenando os elementos da matrix:

        setElements(this.variables, this.id, this.matrix);
    }

    //Setters privados:

    private void setElements(String[] variables, String matrixId, String matrix) {

        //Contando o número de linhas e colunas:

        int rows = 0;
        int columns = 1;

        for(int index = 0; index < matrix.length(); index++){
            if(rows<1 && (matrix.charAt(index)=='_')){
                columns++;
            }else if(matrix.charAt(index)==';' || matrix.charAt(index)==']'){
                rows++;
            }
        }

        //Preenchendo os elementos da matriz:

        this.elements = new Element[rows][columns];

        String elementId;
        String element;
        int lastIndex = 1;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){

                for(int index = lastIndex; index < matrix.length(); index++){

                    if(matrix.charAt(index)=='_' || matrix.charAt(index)==';' || matrix.charAt(index)==']'){

                        element = matrix.substring(lastIndex, index);
                        elementId = String.format("%s{%d,%d}",matrixId, i, j);
                        this.elements[i][j] = new Element(variables, elementId, element);

                        lastIndex = index + 1;
                        break;
                    }
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos de cálculo numérico:

    public double[][] calculate(){

        double[][] result = new double[elements.length][elements[0].length];

        for(int i = 0; i < elements.length; i++){
            for(int j = 0; j < elements[0].length; j++){
                result[i][j] = elements[i][j].calculate();
            }
        }

        return result;
    }

    public double[][] calculate(String[] variables, double[] values){

        double[][] result = new double[elements.length][elements[0].length];

        for(int i = 0; i < elements.length; i++){
            for(int j = 0; j < elements[0].length; j++){
                result[i][j] = elements[i][j].calculate(variables, values);
            }
        }

        return result;
    }

    //----------------------------------------------------------------------------------------------------

    public String matrix2String(double[][] matrix){

        //Convertendo uma matriz em uma String:

        StringBuilder matrixString = new StringBuilder();

        matrixString.append("[");

        for(int i=0; i < matrix.length; i++){

            for(int j=0; j < matrix[0].length; j++){

                if(j < (matrix[0].length-1)){

                    //Se não for a última coluna:
                    matrixString.append(matrix[i][j] + "_");

                }else{

                    //Se for a última coluna:
                    matrixString.append(matrix[i][j]);
                }
            }

            if(i < matrix.length-1){

                //Se for apenas o útimo elemento da linha:
                matrixString.append(";");

            }else{

                //Se for o último elemento da matriz:
                matrixString.append("]");
            }
        }

        return matrixString.toString();
    }

    //----------------------------------------------------------------------------------------------------
}
