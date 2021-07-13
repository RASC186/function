package com.fermi.MathEngine.LinearAlgebra.Operator;

import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.BlockCollector;
import com.fermi.MathEngine.LinearAlgebra.Matrix;

public class Operator {

    //----------------------------------------------------------------------------------------------------

    //Construtor:

    public Operator(){
        //Construtor vazio...
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos de cálculo numérico:

    public double[][] operate(double[][] a, double[][] b, char operation){

        double[][] c;

        switch (operation){
            case '+':
                c = add(a,b);
                break;
            case '-':
                c = subtract(a,b);
                break;
            case '*':
                c = multiply(a,b);
                break;
            case '/':
                c = divide(a,b);
                break;
            case '^':
                c = pow(a,(int)b[0][0]);
                break;

                default:
                    System.out.println(String.format(
                            "Error {Operator.java}! This operator, \"%s\" ,doesn't exists",operation
                            )
                    );
                    c = null;
                    break;
        }

        return c;
    }

    public double[][] transform(String[] variables, double[] values, String transformation){

        BlockCollector blockCollector = new BlockCollector(transformation);
        Matrix matrix;
        String string;

        double[][] result;

        if(transformation.startsWith("[")){

            //Se for uma matriz:
            string = transformation;
            matrix = new Matrix(variables, "M" ,string);
            result = matrix.calculate(variables, values);

        }else if(transformation.startsWith("det(")){

            //Se for um determinante:

            string = blockCollector.getBlockByStartIndex(4);
            matrix = new Matrix(variables, "M" ,string);
            result = new double[][]{{det(matrix.calculate(variables, values))}};

        }else if(transformation.startsWith("transp(")){

            //se for uma transposição:

            string = blockCollector.getBlockByStartIndex(7);
            matrix = new Matrix(variables, "M" ,string);
            result = transp(matrix.calculate(variables, values));

        }else if(transformation.startsWith("inv(")){

            //Se for uma inversão:

            string = blockCollector.getBlockByStartIndex(4);
            matrix = new Matrix(variables, "M" ,string);
            result = inv(matrix.calculate(variables, values));

        }else{

            //Se for uma constante ou uma função:

            string = String.format("[%s]", transformation);
            matrix = new Matrix(variables, "M" ,string);
            result = matrix.calculate(variables, values);
        }

        return result;
    }

    //----------------------------------------------------------------------------------------------------

    public double det(double[][] matrix){

        double result = 0;

        if(matrix.length==1){

            result = matrix[0][0];

        }else if (matrix.length==2){

            result = matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0];

        }else{

            for(int j = 0; j < matrix.length; j++){
                result += matrix[0][j]*cofactor(matrix, 0, j);
            }
        }

        return result;
    }

    public double cofactor(double[][] matrix, double i, double j){

        double[][] newMatrix = new double[matrix.length-1][matrix.length-1];

        for(int row = 0; row < matrix.length; row++){
            for(int column = 0; column < matrix.length; column++) {

                if(row > i && column > j){
                    newMatrix[row-1][column-1] = matrix[row][column];
                }else if(row > i && column < j){
                    newMatrix[row-1][column] = matrix[row][column];
                } else if(row < i && column > j){
                    newMatrix[row][column-1] = matrix[row][column];
                } else if(row < i && column < j){
                    newMatrix[row][column] = matrix[row][column];
                }
            }
        }

        return Math.pow(-1, i+j)*det(newMatrix);
    }

    public double[] principalMinors(double[][] matrix){

        double[] result = new double[matrix.length];
        double[][] newMatrix;

        for(int index = 0; index<result.length;index++){
            newMatrix = new double[matrix.length-index][matrix.length-index];
            for(int i = index; i < matrix.length; i++){
                for(int j = index; j < matrix.length; j++){
                    newMatrix[i-index][j-index] = matrix[i][j];
                }
            }
            result[index] = det(newMatrix);
        }
        return result;
    }

    //----------------------------------------------------------------------------------------------------

    public double[][] transp(double[][] matrix){

        double[][] result = new double[matrix[0].length][matrix.length];

        for(int i = 0; i < matrix.length;i++){
            for(int j = 0; j < matrix[0].length; j++){
                result[j][i] = matrix[i][j];
            }
        }

        return result;
    }

    public double[][] inv(double[][] matrix){

        //Realocando o endereço de memória da variável:

        matrix = matrix.clone();

        //Criando a matriz identidade:

        double[][] invMatrix = new double[matrix.length][matrix.length];

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                if(i==j){
                    invMatrix[i][j] = 1;
                }else{
                    invMatrix[i][j] = 0;
                }
            }
        }

        //Declarando as variáveis auxiliares do processo de inversão:

        double k;
        int column;

        //Escalonando o triângulo inferior da matriz:

        column = 0;

        for(int index = 1; column < (matrix.length-1); index++){

            if(column+index < matrix.length){

                //Definindo a escala da linha operadora:
                k = -matrix[column+index][column]/matrix[column][column];

                //Dando escala à linha operadora:
                for(int j = 0; j < matrix.length; j++){
                    matrix[column][j] = k*matrix[column][j];
                    invMatrix[column][j] = k*invMatrix[column][j];
                }

                //Operando as linhas:
                for(int j = 0; j < matrix.length; j++) {
                    matrix[column+index][j] += matrix[column][j];
                    invMatrix[column+index][j] += invMatrix[column][j];
                }

            }else {

                //Resetando o índice e mudando de coluna:
                index = 0;
                column++;
            }
        }

        //Escalonando o triângulo superior da matriz:

        column = (matrix.length - 1);

        for (int index = 1; column > 0 ; index++){

            if(column-index >= 0){

                //Definindo a escala da linha operadora:
                k = -matrix[column-index][column]/matrix[column][column];

                //Dando escala à linha operadora e operando as linhas:
                for(int j = 0; j < matrix.length; j++){
                    matrix[column][j] = k*matrix[column][j];
                    invMatrix[column][j] = k*invMatrix[column][j];
                }

                //Operando as linhas:
                for(int j = 0; j < matrix.length; j++) {
                    matrix[column-index][j] += matrix[column][j];
                    invMatrix[column-index][j] += invMatrix[column][j];
                }

            }else{

                //Resetando o índice e mudando de coluna:
                index = 0;
                column--;
            }
        }

        //Dando a escala final às linhas da matriz inversa:

        for(int i = 0; i < invMatrix.length; i++){
            for(int j = 0; j < invMatrix.length; j++){
                invMatrix[i][j]/=matrix[i][i];
            }
        }

        return invMatrix;
    }

    public double[][] scale(double scale, double[][] matrix){

        double[][] result = new double[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                result[i][j] = scale*matrix[i][j];
            }
        }

        return result;
    }

    public double[][] add(double[][] matrixA, double[][] matrixB){

        double[][] result = new double[matrixA.length][matrixA[0].length];

        for(int i = 0; i < matrixA.length; i++){
            for(int j = 0; j < matrixA[0].length; j++){
                result[i][j] = matrixA[i][j]+matrixB[i][j];
            }
        }

        return result;
    }

    public double[][] subtract(double[][] matrixA, double[][] matrixB){

        double[][] result = new double[matrixA.length][matrixA[0].length];

        for(int i = 0; i < matrixA.length; i++){
            for(int j = 0; j < matrixA[0].length; j++){
                result[i][j] = matrixA[i][j]-matrixB[i][j];
            }
        }

        return result;
    }

    public double[][] multiply(double[][] matrixA, double[][] matrixB){

        if((matrixA.length == 1 && matrixA[0].length == 1)&&
                matrixB.length == 1 && matrixB[0].length == 1) {

            //Se ambas as matrizes tiverem apenas 1 elemento:

            return new double[][]{{matrixA[0][0] * matrixB[0][0]}};

        }else if(matrixA.length == 1 && matrixA[0].length == 1){

            //Se somente a matriz 'A' tiver apenas 1 elemento:

            return scale(matrixA[0][0],matrixB);

        }else if(matrixB.length == 1 && matrixB[0].length == 1){

            //Se somente a matriz 'B' tiver apenas 1 elemento:

            return scale(matrixB[0][0],matrixA);

        }else if(matrixA.length == matrixB[0].length){

            //Se o número de linhas de 'A' for igual ao de colunas de 'B', e ambas tiverem vários elementos:

            double[][] result = new double[matrixA.length][matrixB[0].length];

            for(int i = 0; i < matrixA.length; i++){
                for(int j = 0; j < matrixB[0].length; j++){
                    for(int index = 0; index < matrixA[0].length; index++){
                        result[i][j] += matrixA[i][index]*matrixB[index][j];
                    }
                }
            }

            return result;
        }

        //Se nenhuma das alternativas anteriores se confirmar:

        return null;
    }

    public double[][] divide(double[][] matrixA, double[][] matrixB){

        double[][] result;

        if(matrixA.length == 1 && matrixA[0].length == 1 &&
                matrixB.length == 1 && matrixB[0].length == 1){

            //Se ambas as matrizes forem 1x1:
            result = new double[][]{{matrixA[0][0]/matrixB[0][0]}};

        }else if(matrixA.length == 1 && matrixA[0].length == 1){

            //Se apenas a matriz A for 1x1 (erro):
            result = null;

        }else if(matrixB.length == 1 && matrixB[0].length == 1){

            //Se apenas a matriz B for 1x1 (erro):
            result = multiply(new double[][]{{1/matrixB[0][0]}},matrixA);

        }else{

            //Se forem duas matrizes:
            result = multiply(inv(matrixB), matrixA);

        }

        return result;
    }

    public double[][] pow(double[][] matrix, int exponent){

        double[][] originalMatrix = matrix.clone();
        matrix = matrix.clone();

        if(exponent<0){

            //Se o expoente for um número negativo inteiro:
            matrix = inv(matrix);
            exponent = Math.abs(exponent);
        }

        for(int i = 1; i < exponent; i++)
            matrix = multiply(matrix,originalMatrix);

        return matrix;
    }

    //----------------------------------------------------------------------------------------------------
}
