package com.fermi.MathEngine.LinearAlgebra.Algebra.Translator;

import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.FunctionCollector;
import com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector.VariableCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    Classe responsável por mapear as variáveis e codificar em a função para o JavaScript.
*/

public class FunctionHandler {

    //--------------------------------------------------------------------------------------------------

    //Variáveis:

    private VariableCollector variableCollector;
    private FunctionCollector functionCollector;

    private Map<String, String> variables;
    private String function, functionId, encodedExpression;


    //--------------------------------------------------------------------------------------------------

    //Construtores:

    public FunctionHandler(String function) {
        setFunction(function);
    }
    public FunctionHandler(String[] variables, String functionId, String expression) {
        setFunction(variables, functionId, expression);
    }

    //--------------------------------------------------------------------------------------------------

    //Getters:

    public String getFunction() {
        return function;
    }

    public String getFunctionId() {
        return functionId;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public String getEncodedExpression() { return encodedExpression; }

    //--------------------------------------------------------------------------------------------------

    //Setters:

    public void setFunction(String function){

        //Armazenando a String da função:

        this.function = function.replaceAll(" ", "");

        //Coletando o título da função:
        String functionTitle = function.substring(
                0, function.indexOf('=')
        ).replaceAll(" ", "");

        //Coletando a expressão algébrica da função:
        String expression = function.substring(
                function.indexOf('=') + 1, function.length()
        ).replaceAll(" ", "");


        //Construindo os objetos de coleta da função:

        this.variableCollector = new VariableCollector(functionTitle);
        this.functionCollector = new FunctionCollector(expression);

        //Armazenando o identificador da função:

        this.functionId = variableCollector.getFunctionId();

        //Criptografando as variáveis:

        this.variables = mapVariables(variableCollector.getVariables());

        //Criptografando as funções internas e a função completa:

        String encryptedExpression;

        if(functionCollector.getAllSpecialFunctions() != null){
            ArrayList<String> encryptedInnerFunctions = encryptInnerFunctions(expression);
            encryptedExpression = encryptExpression(encryptedInnerFunctions, expression);
        }else{
            encryptedExpression = expression;
        }

        //Codificando a função completa para o JavaScript:

        this.encodedExpression = encodeFunction(encryptedExpression);
    }

    public void setFunction(String[] variables, String functionId, String expression){

        //Eliminando os espaços em branco presentes na expressão e no identificador:

        functionId = functionId.replaceAll(" ", "");
        expression = expression.replaceAll(" ", "");

        //Construindo os objetos de coleta da função:

        this.variableCollector = new VariableCollector(variables, functionId);
        this.functionCollector = new FunctionCollector(expression);

        //Armazenando o identificador da função:

        this.functionId = functionId;

        //Armazenando a String da função:

        this.function = variableCollector.getFunctionTitle() + "=" + expression;

        //Mapeando as variáveis:

        this.variables = mapVariables(variableCollector.getVariables());

        //Criptografando as funções internas:

        ArrayList<String> encryptedInnerFunctions = encryptInnerFunctions(expression);

        //Criptografando a função completa:

        String encryptedExpression = encryptExpression(encryptedInnerFunctions, expression);

        //Codificando a função completa para o JavaScript:

        this.encodedExpression = encodeFunction(encryptedExpression);
    }

    //--------------------------------------------------------------------------------------------------

    //Método de criptografia das funções internas:

    private ArrayList<String> encryptInnerFunctions(String expression) {

        ArrayList<String> encryptedInnerFunctions = new ArrayList<>();

        ArrayList<String> innerFunctions = functionCollector.getAllSpecialFunctions();

        if(innerFunctions != null){

            //Coletando as funções modulares para evitar conflitos entre funções as exponenciais de base modular:

            ArrayList<String> absInnerFunctions = new ArrayList<>();

            if (expression.contains("|")){
                absInnerFunctions = functionCollector.getAbsFunctions(functionCollector.getExpression());
            }

            //Ciclo de criptografia das funções internas:

            String encryptedInnerFunction;

            for (int index = 0; index < innerFunctions.size(); index++) {

                if (innerFunctions.get(index).startsWith("#")) {

                    encryptedInnerFunction = encryptInnerConstant(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (absInnerFunctions.contains(innerFunctions.get(index))) {

                    encryptedInnerFunction = encryptInnerAbsFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("log")) {

                    encryptedInnerFunction = encryptInnerLogFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("ln")) {

                    encryptedInnerFunction = encryptInnerNaturalLogFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("exp")) {

                    encryptedInnerFunction = encryptInnerNaturalExponentialFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("sin")) {

                    encryptedInnerFunction = encryptInnerSinFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("cos") &&
                        !innerFunctions.get(index).startsWith("cossec")) {

                    encryptedInnerFunction = encryptInnerCosFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("tan")) {

                    encryptedInnerFunction = encryptInnerTanFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("sec")) {

                    encryptedInnerFunction = encryptInnerSecFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("cossec")) {

                    encryptedInnerFunction = encryptInnerCossecFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("cotan")) {

                    encryptedInnerFunction = encryptInnerCotanFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("asin")) {

                    encryptedInnerFunction = encryptInnerAsinFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("acos")) {

                    encryptedInnerFunction = encryptInnerAcosFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("atan")) {

                    encryptedInnerFunction = encryptInnerAtanFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("sinh")) {

                    encryptedInnerFunction = encryptInnerSinhFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("cosh")) {

                    encryptedInnerFunction = encryptInnerCoshFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("tanh")) {

                    encryptedInnerFunction = encryptInnerTanhFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("asinh")) {

                    encryptedInnerFunction = encryptInnerAsinhFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("acosh")) {

                    encryptedInnerFunction = encryptInnerAcoshFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).startsWith("atanh")) {

                    encryptedInnerFunction = encryptInnerAtanhFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);

                } else if (innerFunctions.get(index).contains("^")){

                    encryptedInnerFunction = encryptInnerPowerFunction(innerFunctions.get(index));
                    encryptedInnerFunctions.add(index, encryptedInnerFunction);
                }
            }

        }else{

            encryptedInnerFunctions = null;
        }

        return encryptedInnerFunctions;
    }

    //Método de Mapeamento das variáveis:

    private Map<String,String> mapVariables(ArrayList<String> variables_ArrayList){

        Map<String, String> variables_Map = new HashMap<>();

        if(variables_ArrayList!=null){

            //Se houverem variáveis:

            for(int index = 0; index < variables_ArrayList.size(); index++){
                variables_Map.put(
                        variables_ArrayList.get(index),
                        String.format("<X%d>", index)
                );
            }

        }else{

            //Se não houverem variáveis:

            variables_Map = null;
        }

        return variables_Map;
    }

    //Método de criptografia da função completa:

    private String encryptExpression(ArrayList<String> encryptedInnerFunctions, String expression) {

        String encryptedExpression = expression;

        //Criptografando as funções internas:

        ArrayList<String> innerFunctions = functionCollector.getAllSpecialFunctions();

        if (innerFunctions != null) {

            //Se houverem funções internas:

            for (int index = 0; index < innerFunctions.size(); index++)
                encryptedExpression = encryptedExpression.replace(
                        innerFunctions.get(index),
                        encryptedInnerFunctions.get(index)
                );

        }

        //Criptografando as variáveis:

        ArrayList<String> variables = variableCollector.getVariables();

        if (variables != null) {

            //Se houverem variáveis na função:

            for (int index = 0; index < variables.size(); index++)
                encryptedExpression = encryptedExpression.replaceAll(
                        variables.get(index),
                        String.format("<X%d>", index)
                );

        }

        return encryptedExpression;
    }

    //Método de codificação da função completa:

    private String encodeFunction(String encryptedExpression) {

        String encodedExpression = encryptedExpression;

        //Codificando as constantes especiais para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#20", "Math.random()");
        encodedExpression = encodedExpression.replaceAll("#21", "Math.PI");
        encodedExpression = encodedExpression.replaceAll("#22", "Math.E");

        //Codificando as funções modulares para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#00", "Math.abs");

        //Codificando as funções de potenciação para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#01", "Math.pow");

        //Codificando as funções logarítmicas genéricas para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#02", "Math.log");

        //Codificando as funções logarítmicas naturais para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#03", "Math.log");

        //Codificando as funções exponeciais naturais para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#04", "Math.exp");

        //Codificando as funções trigonomentricas para o JavaScript
        encodedExpression = encodedExpression.replaceAll("#05", "Math.sin");
        encodedExpression = encodedExpression.replaceAll("#06", "Math.cos");
        encodedExpression = encodedExpression.replaceAll("#07", "Math.tan");
        encodedExpression = encodedExpression.replaceAll("#08", "1/Math.cos");
        encodedExpression = encodedExpression.replaceAll("#09", "1/Math.sin");
        encodedExpression = encodedExpression.replaceAll("#10", "1/Math.tan");

        //Codificando as funções trigonomentricas inversas para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#11", "Math.asin");
        encodedExpression = encodedExpression.replaceAll("#12", "Math.acos");
        encodedExpression = encodedExpression.replaceAll("#13", "Math.atan");

        //Codificando as funções trigonomentricas hiperbólicas para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#14", "Math.sinh");
        encodedExpression = encodedExpression.replaceAll("#15", "Math.cosh");
        encodedExpression = encodedExpression.replaceAll("#16", "Math.tanh");

        //Codificando as funções trigonomentricas hiperbólicas inversas para o JavaScript;
        encodedExpression = encodedExpression.replaceAll("#17", "Math.asinh");
        encodedExpression = encodedExpression.replaceAll("#18", "Math.acosh");
        encodedExpression = encodedExpression.replaceAll("#19", "Math.atanh");

        return encodedExpression;
    }

    //--------------------------------------------------------------------------------------------------

    //Métodos de criptografia das funções matemáticas:

    private String encryptInnerConstant(String f) {

        switch (f) {
            case "#R":
                f = "#20";
                break;

            case "#pi":
                f = "#21";
                break;

            case "#e":
                f = "#22";
                break;
        }

        return f;
    }

    private String encryptInnerAbsFunction(String f) {

        f = f.substring(1, f.length() - 1);
        f = "#00(" + f + ")";

        return f;
    }

    private String encryptInnerPowerFunction(String f) {

        String base = f.substring(0, f.indexOf('^'));
        String exponent = f.substring(f.indexOf('^') + 1, f.length());

        f = "#01(" + base + "," + exponent + ")";

        return f;
    }

    private String encryptInnerLogFunction(String f) {

        //Verificando se há outras funções internas dentro deste log:

        ArrayList<String> innerFunctions = functionCollector.getAllSpecialFunctions();

        for (int index = innerFunctions.indexOf(f); index < innerFunctions.size(); index++) {

            //Criptografando as funções internas para evitar conflitos entre as vírgulas dos logaritmos:

            if (f.contains(innerFunctions.get(index)) && f != innerFunctions.get(index)) {
                //Se a função logarítmica contem a função interna, mas não é igual a ela:
                f = f.replace(innerFunctions.get(index), "<" + index + ">");
            }
        }

        //Criptografando o logaritmo:

        String base = f.substring(4, f.indexOf(','));
        String logarithm = f.substring(f.indexOf(',') + 1, f.length() - 1);

        f = "(#02(" + logarithm + ")/#02(" + base + "))";

        //Descriptografando as funções internas do log:

        for (int index = innerFunctions.indexOf(f); index < innerFunctions.size(); index++) {
            if (f.contains("<" + index + ">")) {
                f = f.replace("<" + index + ">", innerFunctions.get(index));
            }
        }

        return f;
    }

    private String encryptInnerNaturalLogFunction(String f) {

        return "#03" + f.substring(2);

    }

    private String encryptInnerNaturalExponentialFunction(String f) {

        return "#04" + f.substring(3);

    }

    private String encryptInnerSinFunction(String f) {

        return "#05" + f.substring(3);

    }

    private String encryptInnerCosFunction(String f) {

        return "#06" + f.substring(3);

    }

    private String encryptInnerTanFunction(String f) {

        return "#07" + f.substring(3);

    }

    private String encryptInnerSecFunction(String f) {

        return "#08" + f.substring(3);

    }

    private String encryptInnerCossecFunction(String f) {

        return "#09" + f.substring(6);

    }

    private String encryptInnerCotanFunction(String f) {

        return "#10" + f.substring(5);

    }

    private String encryptInnerAsinFunction(String f) {

        return "#11" + f.substring(4);

    }

    private String encryptInnerAcosFunction(String f) {

        return "#12" + f.substring(4);

    }

    private String encryptInnerAtanFunction(String f) {

        return "#13" + f.substring(4);

    }

    private String encryptInnerSinhFunction(String f) {

        return "#14" + f.substring(4);

    }

    private String encryptInnerCoshFunction(String f) {

        return "#15" + f.substring(4);

    }

    private String encryptInnerTanhFunction(String f) {

        return "#16" + f.substring(4);

    }

    private String encryptInnerAsinhFunction(String f) {

        return "#17" + f.substring(5);

    }

    private String encryptInnerAcoshFunction(String f) {

        return "#18" + f.substring(5);

    }

    private String encryptInnerAtanhFunction(String f) {

        return "#19" + f.substring(5);

    }

    //--------------------------------------------------------------------------------------------------
}


