package com.fermi.MathEngine.LinearAlgebra;

//Esta classe é responsável por armazenar os métodos das operações matriciais.

import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.BlockCollector;
import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.FunctionCollector;
import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.VariableCollector;
import com.fermi.MathEngine.LinearAlgebra.Operator.Operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatrixExpression {

    //----------------------------------------------------------------------------------------------------

    //Variáveis:

    private BlockCollector blockCollector;

    private ArrayList<String> variables;
    private String id, title;
    private String expression, encryptedExpression;

    private Map<String, String> matrices = new HashMap<>();
    private Map<String, String> innerExpressions = new HashMap<>();


    //----------------------------------------------------------------------------------------------------

    //Construtores:

    public MatrixExpression(){
        //Construtor vazio...
    }

    public MatrixExpression(String matrixExpression){
        setMatrixOperator(matrixExpression);
    }

    public MatrixExpression(String[] variables, String id, String expression){
        setMatrixOperator(variables, id, expression);
    }

    //----------------------------------------------------------------------------------------------------

    //Getters:

    @Override
    public String toString() {
        return String.format("%s=%s", title, expression);
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public String getTitle() {
        return title;
    }

    public String getExpression() {
        return expression;
    }

    public String getEncryptedExpression() {
        return encryptedExpression;
    }

    //----------------------------------------------------------------------------------------------------

    //Setters:

    public void setMatrixOperator(String matrixExpression){

        matrixExpression = matrixExpression.replaceAll(" ", "");

        String title = matrixExpression.substring(0, matrixExpression.indexOf('='));
        String expression = matrixExpression.substring(matrixExpression.indexOf('=')+1);

        VariableCollector variableCollector = new VariableCollector(title);

        this.id = variableCollector.getFunctionId();
        this.variables = variableCollector.getVariables();
        this.title = title;
        this.expression = expression;

        encodeExpression();
        searchSubExpressions();
    }

    public void setMatrixOperator(String[] variables, String id, String expression){

        VariableCollector variableCollector = new VariableCollector(variables, id);

        this.id = id;
        this.variables = variableCollector.getVariables();
        this.title = variableCollector.getFunctionTitle();
        this.expression = expression.replaceAll(" ", "");

        encodeExpression();
        searchSubExpressions();
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos de interpretação da função matricial:

    public void encodeExpression(){

        //Codificando as matrizes:

        blockCollector = new BlockCollector(expression);

        encryptedExpression = expression;

        String matrixId, matrix;

        for(int index = 0; index < expression.length(); index++){

            if(expression.charAt(index)=='['){

                //Se for o primeiro caractere de uma matriz:

                matrix = blockCollector.getBlockByStartIndex(index);
                matrixId = String.format("<M%d>",matrices.size());

                matrices.put(
                        matrixId,
                        matrix
                );

                encryptedExpression = encryptedExpression.replace(matrix, matrixId);
            }
        }

        //Codificando as funções especiais:

        FunctionCollector functionCollector = new FunctionCollector(encryptedExpression);
        ArrayList<String> specialFunctions = functionCollector.getAllSpecialFunctions();

        if(specialFunctions == null){

            //Se não houverem funções especiais:

        }else{

            //Se houverem funções especiais:

            for(int index = 0; index < specialFunctions.size(); index++) {

                matrix = specialFunctions.get(index);

                if(!matrix.contains("<")){

                    matrixId = String.format("<M%d>",matrices.size());

                    matrices.put(
                            matrixId,
                            matrix
                    );

                    encryptedExpression = encryptedExpression.replace(matrix, matrixId);
                }
            }

            //Codificando as funções

            blockCollector = new BlockCollector(encryptedExpression);
            String encodedExpressionCopy = encryptedExpression;

            boolean bool;

            for(int index = 0; index < encodedExpressionCopy.length(); index++) {

                if(encodedExpressionCopy.charAt(index)=='('){

                    //Se o caractere do índice for o início de um bloco entre parenteses:

                    matrix = blockCollector.getBlockByStartIndex(index);

                    if(!matrix.contains("<")){

                        //Se a string do bloco não tiver matrizes em seu interior:

                        matrixId = String.format("<M%d>",matrices.size());

                        matrices.put(
                                matrixId,
                                matrix
                        );

                        encryptedExpression = encryptedExpression.replace(matrix, matrixId);
                    }


                }else if((index==0 ||
                        encodedExpressionCopy.charAt(index)=='+' ||
                        encodedExpressionCopy.charAt(index)=='-' ||
                        encodedExpressionCopy.charAt(index)=='*' ||
                        encodedExpressionCopy.charAt(index)=='^') &&
                        encodedExpressionCopy.charAt(index+1)!='('){

                    //Se for o primeiro caractere da String ou um simbolo operador:

                    int startIndex = index;

                    if(encodedExpressionCopy.charAt(index)=='^'){
                        //Se for uma exponenciação:
                        index++;
                    }

                    do{
                        index++;

                        bool = (index == encodedExpressionCopy.length()) || (encodedExpressionCopy.charAt(index)=='+' ||
                                encodedExpressionCopy.charAt(index)=='-' || encodedExpressionCopy.charAt(index)=='*' ||
                                encodedExpressionCopy.charAt(index)=='^' || encodedExpressionCopy.charAt(index)==')');

                    }while(!bool);

                    //Coletando a string entre os dois sinais:
                    matrix = encodedExpressionCopy.substring(startIndex, index);

                    if(!matrix.contains("<")){

                        //Se entre os dois sinais não houverem funções internas ou matrizes:

                        if(startIndex==0){

                            //Se for o primeiro elemento da expressão matricial:

                            matrixId = String.format("<M%d>",matrices.size());

                            matrices.put(matrixId, matrix);

                            encryptedExpression = encryptedExpression.replaceFirst(matrix, matrixId);

                        }else{

                            //Se não for o primeiro elemento da expressão matricial:

                            String operator;

                            matrix = matrix.substring(1);
                            operator = String.valueOf(encodedExpressionCopy.charAt(startIndex));

                            matrixId = String.format("<M%d>",matrices.size());

                            matrices.put(matrixId, matrix);

                            encryptedExpression = encryptedExpression.replace(
                                    operator + matrix,
                                    operator + matrixId);
                        }
                    }

                    index = startIndex;
                }
            }
        }

    }

    public void searchSubExpressions(){

        blockCollector = new BlockCollector(encryptedExpression);

        String subExpressionId, subExpression;
        int subExpressionIndex = 0;

        //Se o primeiro caractere é um parenteses, ocorre um problema;

        for(int index = 0; index < encryptedExpression.length(); index++){

            if(index==0){

                //Se for o primeiro caractere da função codificada:

                subExpressionId = String.format("<IE%d>", subExpressionIndex);
                subExpression = encryptedExpression;

                innerExpressions.put(
                        subExpressionId,
                        subExpression
                );

                subExpressionIndex++;
            }

            if(encryptedExpression.charAt(index)=='('){

                //Se o caractere do índice for um parentese:

                subExpressionId = String.format("<IE%d>", subExpressionIndex);
                subExpression = blockCollector.getBlockByStartIndex(index);

                innerExpressions.put(
                        subExpressionId,
                        subExpression
                );

                subExpressionIndex++;
            }
        }
    }

    //----------------------------------------------------------------------------------------------------

    //Método principal de cálculo matricial:

    public double[][] calculate(String[] variables, double[] values){

        String[] previousResult = new String[innerExpressions.size()];

        String innerExpression;

        for(int index = innerExpressions.size()-1; index>=0; index--){

            innerExpression = innerExpressions.get(String.format("<IE%d>", index));

            if(index < innerExpressions.size()-1){
                for(int k = index+1; k < innerExpressions.size(); k++){
                    innerExpression = innerExpression.replace(
                            innerExpressions.get(String.format("<IE%d>", k)),
                            previousResult[k]
                    );
                }
            }

            previousResult[index] = operateInnerExpression(variables, values, innerExpression);
        }

        return new Operator().transform(variables,values,matrices.get(previousResult[0]));
    }

    //Métodos auxiliares do cálculo matricial:

    private String operateInnerExpression(String[] variables, double[] values, String innerExpression){

        Operator operator = new Operator();
        Matrix matrix = new Matrix();

        String[] a, b;
        String matrixId;
        boolean isItAnOperator;

        //Operando as exponenciações:

        for(int index = 0; index < innerExpression.length(); index++){

            isItAnOperator = innerExpression.charAt(index)=='^';

            if(isItAnOperator){

                double[][] mA, mB, mC;

                a = getA(innerExpression, index);
                b = getB(innerExpression, index);

                mA = operator.transform(variables, values, a[1]);
                mB = operator.transform(variables, values, b[1]);
                mC = operator.operate(mA, mB, '^');

                matrixId = String.format("<M%d>", matrices.size());

                matrices.put(matrixId, matrix.matrix2String(mC));

                index=-1;
                innerExpression = innerExpression.replace(a[0] + "^" + b[0], matrixId);
            }
        }

        //Operando os produtos:

        for(int index = 0; index < innerExpression.length(); index++){

            isItAnOperator = innerExpression.charAt(index)=='*';

            if(isItAnOperator){

                double[][] mA, mB, mC;

                a = getA(innerExpression, index);
                b = getB(innerExpression, index);

                mA = operator.transform(variables, values, a[1]);
                mB = operator.transform(variables, values, b[1]);
                mC = operator.operate(mA, mB, '*');

                matrixId = String.format("<M%d>", matrices.size());

                matrices.put(matrixId, matrix.matrix2String(mC));

                index=-1;
                innerExpression = innerExpression.replace(a[0] + "*" + b[0], matrixId);
            }
        }

        //Operando as divisões:

        for(int index = 0; index < innerExpression.length(); index++){

            isItAnOperator = innerExpression.charAt(index)=='/';

            if(isItAnOperator){

                double[][] mA, mB, mC;

                a = getA(innerExpression, index);
                b = getB(innerExpression, index);

                mA = operator.transform(variables, values, a[1]);
                mB = operator.transform(variables, values, b[1]);
                mC = operator.operate(mA, mB, '/');

                matrixId = String.format("<M%d>", matrices.size());

                matrices.put(matrixId, matrix.matrix2String(mC));

                index=-1;
                innerExpression = innerExpression.replace(a[0] + "/" + b[0], matrixId);
            }
        }

        //Operando as somas e subtrações:

        for(int index = 0; index < innerExpression.length(); index++){

            isItAnOperator = innerExpression.charAt(index)=='+' || innerExpression.charAt(index)=='-';

            if(isItAnOperator){

                double[][] mA, mB, mC;

                a = getA(innerExpression, index);
                b = getB(innerExpression, index);

                mA = operator.transform(variables, values, a[1]);
                mB = operator.transform(variables, values, b[1]);
                mC = operator.operate(mA, mB, innerExpression.charAt(index));

                matrixId = String.format("<M%d>", matrices.size());

                matrices.put(matrixId, matrix.matrix2String(mC));

                innerExpression = innerExpression.replace(
                        a[0] + innerExpression.charAt(index) + b[0],
                        matrixId
                );

                index=-1;
            }
        }

        //Retirando os parenteses inicial e final, se existirem:

        if(innerExpression.charAt(0) == '('){
            innerExpression = innerExpression.substring(1, innerExpression.length()-1);
        }

        return innerExpression;
    }

    private String[] getA(String innerFunction, int operatorIndex){

        blockCollector = new BlockCollector(innerFunction);

        String matrixId;

        int startIndex;
        int finalIndex = operatorIndex-1;

        String[] a = new String[]{"?", "!"};

        if(operatorIndex==0 || innerFunction.charAt(operatorIndex-1) == '('){

            a[0] = "";
            a[1] = "[0]";

        }else if(innerFunction.charAt(finalIndex)=='>'){

            startIndex = blockCollector.getBlockStartIndex(finalIndex);
            matrixId = innerFunction.substring(startIndex,finalIndex+1);

            if(innerFunction.startsWith("det<",startIndex-3)){

                a[0] = String.format("det%s", matrixId);
                a[1] = String.format("det(%s)", matrices.get(matrixId));

            }else if(innerFunction.startsWith("inv<",startIndex-3)){

                a[0] = String.format("inv%s", matrixId);
                a[1] = String.format("inv(%s)", matrices.get(matrixId));

            }else if(innerFunction.startsWith("transp<",startIndex-6)){

                a[0] = String.format("transp%s", matrixId);
                a[1] = String.format("transp(%s)", matrices.get(matrixId));

            }else{

                a[0] = matrixId;
                a[1] = matrices.get(matrixId);
            }
        }

        return a;
    }

    private String[] getB(String innerFunction, int operatorIndex){

        blockCollector = new BlockCollector(innerFunction);

        String matrixId;

        int startIndex = operatorIndex+1;
        int finalIndex;

        String[] b =new String[]{"?","!"};

        if(innerFunction.charAt(startIndex)=='<') {

            finalIndex = blockCollector.getBlockFinalIndex(startIndex);
            matrixId = innerFunction.substring(startIndex, finalIndex+1);

            b[0] = matrixId;
            b[1] = matrices.get(matrixId);

        }else if(innerFunction.startsWith("det<",startIndex)){

            finalIndex = blockCollector.getBlockFinalIndex(startIndex+3);
            matrixId = innerFunction.substring(startIndex+3, finalIndex+1);

            b[0] = String.format("det%s", matrixId);
            b[1] = String.format("det(%s)", matrices.get(matrixId));

        }else if(innerFunction.startsWith("inv<",startIndex)){

            finalIndex = blockCollector.getBlockFinalIndex(startIndex+3);
            matrixId = innerFunction.substring(startIndex+3, finalIndex+1);

            b[0] = String.format("inv%s", matrixId);
            b[1] = String.format("inv(%s)", matrices.get(matrixId));

        }else if(innerFunction.startsWith("transp<",startIndex)){

            finalIndex = blockCollector.getBlockFinalIndex(startIndex+6);
            matrixId = innerFunction.substring(startIndex+6, finalIndex+1);

            b[0] = String.format("transp%s", matrixId);
            b[1] = String.format("transp(%s)", matrices.get(matrixId));
        }

        return b;
    }

    //----------------------------------------------------------------------------------------------------
}
