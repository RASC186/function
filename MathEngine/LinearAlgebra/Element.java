package com.fermi.MathEngine.LinearAlgebra;

import com.fermi.MathEngine.LinearAlgebra.Algebra.Function;

import java.util.Map;

public class Element {

    //----------------------------------------------------------------------------------------------------

    //Variáveis:

    private Function element;

    //----------------------------------------------------------------------------------------------------

    //Construtores:

    public Element(String[] variables, String elementId, String expression){
        setElement(variables, elementId, expression);
    }

    //----------------------------------------------------------------------------------------------------

    //Getters:

    @Override
    public String toString() {
        return element.toString();
    }

    public String getId() {
        return element.getId();
    }

    public Map<String, String> getVariables() { return element.getVariables(); }

    //----------------------------------------------------------------------------------------------------

    //Setters:

    public void setElement(String[] variables,  String elementId, String expression){
        this.element = new Function(variables, elementId, expression);
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos de cálculo numérico:

    public double calculate(){

        return this.element.calculate();
    }

    public double calculate(String[] variables, double[] values){

        return this.element.calculate(variables, values);
    }

    //----------------------------------------------------------------------------------------------------
}
