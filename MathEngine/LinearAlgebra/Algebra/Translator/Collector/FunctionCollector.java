package com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector;

import java.util.ArrayList;

/*
    Classe responsável por identificar, coletar e ordenar as funções matemáticas que estão presentes na
    String de uma função.
*/

//OBS.: A expressão matemática passada como argumento não deve conter espaços em branco.

public class FunctionCollector {

    //--------------------------------------------------------------------------------------------------

    //Variáveis:

    private ArrayList<String> specialFunctions;
    private String expression;

    //--------------------------------------------------------------------------------------------------

    //Construtor:

    public FunctionCollector(){
        //Construtor vazio...
    }

    public FunctionCollector(String expression){
        setFunction(expression);
    }

    //--------------------------------------------------------------------------------------------------

    //Setters:

    public void setFunction(String expression) {

        this.expression = expression;
        this.specialFunctions = getAllSpecialFunctions(expression);

    }

    //--------------------------------------------------------------------------------------------------

    //Getters:

    public String getExpression() { return expression; }

    public ArrayList<String> getAllSpecialFunctions() {
        return specialFunctions;
    }

    public ArrayList<String> getAllSpecialFunctions(String expression) {

        //Coletando as funções especiais internas:

        ArrayList<String> innerFunctions = new ArrayList<>();

        if (expression.contains("#"))
            innerFunctions.addAll(getSpecialConstants(expression));
        if (expression.contains("|"))
            innerFunctions.addAll(getAbsFunctions(expression));
        if (expression.contains("^"))
            innerFunctions.addAll(getPowerFunctions(expression));
        if (expression.contains("log"))
            innerFunctions.addAll(getLogFunctions(expression));
        if (expression.contains("ln"))
            innerFunctions.addAll(getNaturalLogFunctions(expression));
        if (expression.contains("exp"))
            innerFunctions.addAll(getNaturalExponetialFunctions(expression));
        if (expression.contains("sin"))
            innerFunctions.addAll(getSinFunctions(expression));
        if (expression.contains("cos"))
            innerFunctions.addAll(getCosFunctions(expression));
        if (expression.contains("tan"))
            innerFunctions.addAll(getTanFunctions(expression));
        if (expression.contains("sec"))
            innerFunctions.addAll(getSecFunctions(expression));
        if (expression.contains("cossec"))
            innerFunctions.addAll(getCossecFunctions(expression));
        if (expression.contains("cotan"))
            innerFunctions.addAll(getCotanFunctions(expression));
        if (expression.contains("asin"))
            innerFunctions.addAll(getAsinFunctions(expression));
        if (expression.contains("acos"))
            innerFunctions.addAll(getAcosFunctions(expression));
        if (expression.contains("atan"))
            innerFunctions.addAll(getAtanFunctions(expression));
        if (expression.contains("sinh"))
            innerFunctions.addAll(getSinhFunctions(expression));
        if (expression.contains("cosh"))
            innerFunctions.addAll(getCoshFunctions(expression));
        if (expression.contains("tanh"))
            innerFunctions.addAll(getTanhFunctions(expression));
        if (expression.contains("asinh"))
            innerFunctions.addAll(getAsinhFunctions(expression));
        if (expression.contains("acosh"))
            innerFunctions.addAll(getAcoshFunctions(expression));
        if (expression.contains("atanh"))
            innerFunctions.addAll(getAtanhFunctions(expression));

        //Ordenando as funções especiais internas em ordem de substituição:

        ArrayList<String> innerFunctionsInReplecementOrder = new ArrayList<>();

        if(innerFunctions.size() != 0){
            innerFunctionsInReplecementOrder.add(0, innerFunctions.get(0));
        }else{
            innerFunctionsInReplecementOrder = null;
        }

        for (int i = 1; i < innerFunctions.size(); i++) {

            for (int j = innerFunctionsInReplecementOrder.size() - 1; j >= 0; j--) {

                if (innerFunctionsInReplecementOrder.get(j).contains(innerFunctions.get(i))
                        && innerFunctionsInReplecementOrder.get(j) != innerFunctions.get(i)) {

                    //Se a posição j do vetor ordenado contem a posição i do vetor desordenado, mas são diferentes:
                    innerFunctionsInReplecementOrder.add(j + 1, innerFunctions.get(i));
                    break;

                } else if (innerFunctionsInReplecementOrder.get(j) == innerFunctions.get(i)) {

                    //Se a posição j do vetor ordenado é igual a posição i do vetor desordenado:
                    innerFunctionsInReplecementOrder.add(j + 1, innerFunctions.get(i));
                    break;

                } else if (j == 0) {

                    //Se nenhuma da outras alternativas se confirmarem até o fim do loop:
                    innerFunctionsInReplecementOrder.add(0, innerFunctions.get(i));
                    break;
                }
            }
        }

        return innerFunctionsInReplecementOrder;
    }

    //--------------------------------------------------------------------------------------------------

    //Métodos públicos coletores de funções especiais:

    public ArrayList<String> getSpecialConstants(String expression) {

        ArrayList<String> specialConstants = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == '#') {

                if (expression.startsWith("#pi", index)) {
                    specialConstants.add("#pi");

                } else if (expression.startsWith("#e", index)) {
                    specialConstants.add("#e");

                } else if (expression.startsWith("#R", index)) {
                    specialConstants.add("#R");
                }
            }
        }

        return specialConstants;
    }

    public ArrayList<String> getAbsFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> absFunctions = new ArrayList<>();

        int lastOpenBarIndex = -1;

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == '|') {

                if (index == 0) {

                    lastOpenBarIndex = index;
                    absFunctions.add(blockCollector.getBlockByStartIndex(index));

                } else if ("+-*/^%([{,".contains(String.valueOf(expression.charAt(index-1)))) {

                    lastOpenBarIndex = index;
                    absFunctions.add(blockCollector.getBlockByStartIndex(index));

                } else if (expression.charAt(index-1) == '|' && lastOpenBarIndex == (index - 1)) {

                    lastOpenBarIndex = index;
                    absFunctions.add(blockCollector.getBlockByStartIndex(index));
                }
            }
        }

        return absFunctions;
    }

    public ArrayList<String> getPowerFunctions(String expression) {

        ArrayList<String> bases = getPowerBases(expression);
        ArrayList<String> exponents = getPowerExponents(expression);

        ArrayList<String> powerFunctions = new ArrayList<>();

        for (int index = 0; index < bases.size(); index++) {
            powerFunctions.add(bases.get(index) + "^" + exponents.get(index));
        }

        return powerFunctions;
    }

    public ArrayList<String> getLogFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> logFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'l' && expression.startsWith("log(", index)) {

                logFunctions.add("log" + blockCollector.getBlockByStartIndex(index + 3));
            }
        }

        return logFunctions;
    }

    public ArrayList<String> getNaturalLogFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> lnFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'l' && expression.startsWith("ln(", index)) {

                lnFunctions.add("ln" + blockCollector.getBlockByStartIndex(index + 2));
            }
        }

        return lnFunctions;
    }

    public ArrayList<String> getNaturalExponetialFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> expFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'e' && expression.startsWith("exp(", index)) {

                expFunctions.add("exp" + blockCollector.getBlockByStartIndex(index + 3));
            }
        }

        return expFunctions;
    }

    public ArrayList<String> getSinFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> sinFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index == 0 && expression.charAt(index)  == 's' && expression.startsWith("sin(", index)) {

                sinFunctions.add("sin" + blockCollector.getBlockByStartIndex(index + 3));

            } else if (index != 0 && expression.charAt(index-1)  != 'a' &&
                    expression.charAt(index)  == 's' && expression.startsWith("sin(", index)) {
                sinFunctions.add("sin" + blockCollector.getBlockByStartIndex(index + 3));
            }
        }

        return sinFunctions;
    }

    public ArrayList<String> getCosFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> cosFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index == 0 && expression.charAt(index)  == 'c' && expression.startsWith("cos(", index)) {
                    cosFunctions.add("cos" + blockCollector.getBlockByStartIndex(index + 3));

            } else if (index != 0 && expression.charAt(index - 1) != 'a' &&
                    expression.charAt(index) == 'c' && expression.startsWith("cos(", index)) {
                    cosFunctions.add("cos" + blockCollector.getBlockByStartIndex(index + 3));
            }
        }

        return cosFunctions;
    }

    public ArrayList<String> getTanFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> tanFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index == 0 &&
                    expression.charAt(index) == 't' &&
                    expression.startsWith("tan(", index)) {

                tanFunctions.add("tan" + blockCollector.getBlockByStartIndex(index + 3));

            } else if (index != 0 && expression.charAt(index) == 't' &&
                    expression.startsWith("tan(", index) &&
                    expression.charAt(index-1) != 'a' && expression.charAt(index-1) != 'o') {

                tanFunctions.add("tan" + blockCollector.getBlockByStartIndex(index + 3));
            }
        }

        return tanFunctions;
    }

    public ArrayList<String> getSecFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> secFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index < 3 && expression.charAt(index) == 's' && expression.startsWith("sec(", index)) {

                    secFunctions.add("sec" + blockCollector.getBlockByStartIndex(index + 3));

            } else if (expression.charAt(index) == 's' && expression.startsWith("sec(", index) &&
                    !expression.startsWith("cossec", index - 3)) {

                    secFunctions.add("sec" + blockCollector.getBlockByStartIndex(index + 3));
            }
        }

        return secFunctions;
    }

    public ArrayList<String> getCossecFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> cossecFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'c' && expression.startsWith("cossec(", index)) {

                cossecFunctions.add("cossec" + blockCollector.getBlockByStartIndex(index + 6));
            }
        }

        return cossecFunctions;
    }

    public ArrayList<String> getCotanFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> cotanFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'c' && expression.startsWith("cotan(", index)) {

                cotanFunctions.add("cotan" + blockCollector.getBlockByStartIndex(index + 5));
            }
        }

        return cotanFunctions;
    }

    public ArrayList<String> getAsinFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> asinFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'a' && expression.startsWith("asin(", index)) {

                asinFunctions.add("asin" + blockCollector.getBlockByStartIndex(index + 4));
            }
        }

        return asinFunctions;
    }

    public ArrayList<String> getAcosFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> acosFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'a' && expression.startsWith("acos(", index)) {

                acosFunctions.add("acos" + blockCollector.getBlockByStartIndex(index + 4));
            }
        }

        return acosFunctions;
    }

    public ArrayList<String> getAtanFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> atanFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'a' && expression.startsWith("atan(", index)) {

                atanFunctions.add("atan" + blockCollector.getBlockByStartIndex(index + 4));
            }
        }

        return atanFunctions;
    }

    public ArrayList<String> getSinhFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> sinhFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index == 0 && expression.charAt(index) == 's' && expression.startsWith("sinh(", index)) {

                sinhFunctions.add("sinh" + blockCollector.getBlockByStartIndex(index + 4));

            } else if (index != 0 && expression.charAt(index) == 's' && expression.charAt(index-1) != 'a'
                     && expression.startsWith("sinh(", index)) {

                sinhFunctions.add("sinh" + blockCollector.getBlockByStartIndex(index + 4));
            }
        }

        return sinhFunctions;
    }

    public ArrayList<String> getCoshFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> coshFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index == 0 && expression.charAt(index) == 'c' && expression.startsWith("cosh(", index)) {

                coshFunctions.add("cosh" + blockCollector.getBlockByStartIndex(index + 4));

            } else if (index != 0 && expression.charAt(index) == 'c' && expression.charAt(index-1) != 'a'
                    && expression.startsWith("cosh(", index)) {

                coshFunctions.add("cosh" + blockCollector.getBlockByStartIndex(index + 4));
            }
        }

        return coshFunctions;
    }

    public ArrayList<String> getTanhFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> tanhFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (index == 0 && expression.charAt(index) == 't' && expression.startsWith("tanh(", index)) {

                tanhFunctions.add("tanh" + blockCollector.getBlockByStartIndex(index + 4));

            } else if (index != 0 && expression.charAt(index) == 't' && expression.charAt(index-1) == 'a' &&
                    expression.startsWith("tanh(", index)) {

                tanhFunctions.add("tanh" + blockCollector.getBlockByStartIndex(index + 4));
            }
        }

        return tanhFunctions;
    }

    public ArrayList<String> getAsinhFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> asinhFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'a' && expression.startsWith("asinh(", index)) {

                asinhFunctions.add("asinh" + blockCollector.getBlockByStartIndex(index + 5));
            }
        }

        return asinhFunctions;
    }

    public ArrayList<String> getAcoshFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> acoshFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'a' && expression.startsWith("acosh(", index)) {

                acoshFunctions.add("acosh" + blockCollector.getBlockByStartIndex(index + 5));
            }
        }

        return acoshFunctions;
    }

    public ArrayList<String> getAtanhFunctions(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> atanhFunctions = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++) {

            if (expression.charAt(index) == 'a' && expression.startsWith("atanh(", index)) {

                atanhFunctions.add("atanh" + blockCollector.getBlockByStartIndex(index + 5));
            }
        }

        return atanhFunctions;
    }

    //Métodos privados de coleta das bases e expoentes de funções exponenciais:

    private ArrayList<String> getPowerBases(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> bases = new ArrayList<>();

        //Coletando as posições das setas de exponenciação:

        ArrayList<Integer> powerArrowsIndexes = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++){
            if (expression.charAt(index) == '^'){
                powerArrowsIndexes.add(index);
            }
        }

        //Coletando as bases das exponenciações:

        for (int powerArrowIndex : powerArrowsIndexes) {

            if (")|}>".contains(String.valueOf(expression.charAt(powerArrowIndex - 1)))) {

                //Se a base for uma expressão entre barras de módulo, parênteses ou chaves;
                String base = blockCollector.getBlockByFinalIndex(powerArrowIndex - 1);
                bases.add(base);

            } else {

                //Se a base for uma variável ou um número

                for (int index = powerArrowIndex-1; index >= 0; index--) {

                    if (index == 0 || "+-*/^,(|{".contains(String.valueOf(expression.charAt(index)))) {

                        if (index == 0 && !"(|{".contains(String.valueOf(expression.charAt(index)))) {
                            //Se o 1º elemento da base for o 1º elemento da string e o mesmo for um sinal;
                            bases.add(expression.substring(index, powerArrowIndex));
                            break;
                        } else {
                            //Para qualquer outro caso
                            bases.add(expression.substring(index + 1, powerArrowIndex));
                            break;
                        }
                    }
                }
            }
        }

        return bases;
    }

    private ArrayList<String> getPowerExponents(String expression) {

        BlockCollector blockCollector = new BlockCollector(expression);

        ArrayList<String> exponents = new ArrayList<>();

        //Coletando os índices das setas de exponenciação:

        ArrayList<Integer> powerArrowsIndexes = new ArrayList<>();

        for (int index = 0; index < expression.length(); index++){
            if (expression.charAt(index) == '^'){
                powerArrowsIndexes.add(index);
            }
        }

        //Coletando os expoentes das exponenciações:

        int lastExponentIndex = -1;

        for (int powerArrowIndex : powerArrowsIndexes) {

            if ("(|{".contains(String.valueOf(expression.charAt(powerArrowIndex + 1)))) {

                //Se o que estiver imediatamente após a seta de exponenciação for um blocos;
                lastExponentIndex = blockCollector.getBlockFinalIndex(powerArrowIndex + 1);

            } else if (!"+-".contains(String.valueOf(expression.charAt(powerArrowIndex + 1)))) {

                //Se o que estiver imediatamente após a seta de exponenciação for um número ou uma variável;
                for (int index = powerArrowIndex + 1; index < expression.length() &&
                        !"+-*/^,)|}".contains(String.valueOf(expression.charAt(index))); index++) {
                    lastExponentIndex = index;
                }

            }else if ("+-".contains(String.valueOf(expression.charAt(powerArrowIndex + 1)))) {

                //Se o que estiver imediatamente após a seta de exponenciação for um sinal;

                if ("(|{".contains(String.valueOf(expression.charAt(powerArrowIndex + 2)))) {

                    //Se o que estiver imediatamente após o sinal for um blocos;
                    lastExponentIndex = blockCollector.getBlockFinalIndex(powerArrowIndex + 2);

                } else {

                    //Se o que estiver imediatamente após o sinal for um número ou uma variável;
                    for(int index = powerArrowIndex + 2;  index < expression.length() &&
                            !"+-*/^,)|}".contains(String.valueOf(expression.charAt(index))); index++) {
                        lastExponentIndex = index;
                    }
                }
            }

            if(lastExponentIndex+1 < expression.length()
                    && expression.charAt(lastExponentIndex+1) == '^') {

                //Se o i-ésimo elemento do expoente for a base de um outro expoente;

                String innerExponentBase = expression.substring(powerArrowIndex+1, lastExponentIndex+1);

                String fSubstring = expression.substring(lastExponentIndex+1);
                String secondDegreeExponents = getPowerExponents(fSubstring).get(0);

                exponents.add(innerExponentBase + "^" + secondDegreeExponents);

            }else{

                //Se o i-ésimo elemento do expoente não for a base de um outro expoente;

                exponents.add(expression.substring(powerArrowIndex+1, lastExponentIndex+1));
            }
        }

        return exponents;

    }

    //--------------------------------------------------------------------------------------------------
}
