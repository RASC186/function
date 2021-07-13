package com.fermi.MathEngine.LinearAlgebra.Algebra;

import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.FunctionHandler;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.util.Map;

//Federal University of Bahia;
//Ramon Andrade Sousa Carvalho;
//Student of Electrical Engineering;
//Start: 2017/12/26;

/*
    Classe responsável por calcular o resultado de funções algébricas/numéricas gravadas em String.
*/

public class Function {

    //----------------------------------------------------------------------------------------------------

    //Variáveis:

    private ScriptEngine engine;

    private Map<String,String> variables;
    private String id, function, encodedExpression;

    //----------------------------------------------------------------------------------------------------

    //Construtores:

    public Function(){
        this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public Function(String function) {
        this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
        setFunction(function);
    }

    public Function(String[] variables, String id, String expression) {
        this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
        setFunction(variables, id, expression);
    }

    //----------------------------------------------------------------------------------------------------

    //Getters:

    @Override
    public String toString() { return function; }

    public String getId() { return id; }

    public Map<String, String> getVariables() { return variables; }

    private String getEncodedExpression() { return encodedExpression; }

    private String getEncodedExpression(String[] variables, double[] values){

        //Criando a variável que armazenará a Expressão numérica codificada em JavaScript:
        String encodedExpression = this.encodedExpression;

        //Substituindo as variáveis pelos valores de entrada:

        if(variables!=null && variables.length > 0){

            //Se o vetor das variáveis argumentado não for nulo e tiver pelo menos um elemento:

            for (int index = 0; index < variables.length; index++) {
                encodedExpression = encodedExpression.replaceAll(
                        this.variables.get(variables[index]),
                        String.valueOf(values[index])
                );
            }

        }

        return encodedExpression;
    }

    //----------------------------------------------------------------------------------------------------

    //Setters:

    public void setFunction(String function) {

        FunctionHandler functionHandler = new FunctionHandler(function);

        this.id = functionHandler.getFunctionId();
        this.variables = functionHandler.getVariables();
        this.function = function;
        this.encodedExpression = functionHandler.getEncodedExpression();
    }

    public void setFunction(String[] variables, String functionId, String expression) {

        FunctionHandler functionHandler = new FunctionHandler(variables, functionId, expression);

        this.id = functionId;
        this.variables = functionHandler.getVariables();
        this.function = functionHandler.getFunction();
        this.encodedExpression = functionHandler.getEncodedExpression();
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos de cálculo numérico:

    public double calculate(String[] variables, double[] values) {

        try {

            Double result = Double.parseDouble(engine.eval(

                    getEncodedExpression(variables, values)

            ).toString());

            return result;

        } catch (Exception e) {

            System.out.println("Error {Algebra.Calculate()}! " + e.getMessage());
        }

        return 404;
    }

    public double calculate() {

        try {

            Double result = Double.parseDouble(engine.eval(

                    getEncodedExpression()

            ).toString());

            return result;

        } catch (Exception e) {

            System.out.println("Error {Algebra.Calculate()}! " + e.getMessage());
        }

        return 404;
    }

    //----------------------------------------------------------------------------------------------------
}

























