package com.fermi.MathEngine;

//Classe capaz de localizar raizes de funções e sistemas não-lineares.

import com.fermi.MathEngine.LinearAlgebra.Algebra.Function;

public class Root {

    //----------------------------------------------------------------------------------------------------

    //Variáveis:

    Function function;

    //----------------------------------------------------------------------------------------------------

    //Construtores:

    public Root(String function){
        setFunction(function);
    }

    public Root(String[] variables, String functionId, String expression){
        setFunction(variables, functionId, expression);
    }

    //----------------------------------------------------------------------------------------------------

    //Setters

    public void setFunction(String function) {
        this.function = new Function(function);
    }

    public void setFunction(String[] variables, String functionId, String expression){
        this.function = new Function(variables,functionId,expression);
    }

    //----------------------------------------------------------------------------------------------------

    //Métodos de Busca:




}
