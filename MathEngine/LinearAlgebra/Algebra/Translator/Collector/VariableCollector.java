package com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector;

import java.util.ArrayList;

/*
    Esta classe é responsável por:
        I)   Coletar e ordenar as variáveis em ordem de substituição;
        II)  Coletar o identificador da função; e
        III) Coletar o título da função.
*/

//OBS.: As String's passadas como argumento não devem conter espaços em branco.

public class VariableCollector {

    //--------------------------------------------------------------------------------------------------

    //Variáveis:

    private String functionTitle, functionId;
    private ArrayList<String> variables;

    //--------------------------------------------------------------------------------------------------

    //Construtor:

    public VariableCollector(String functionTitle){ setVariableGetter(functionTitle); }

    public VariableCollector(String[] variables, String functionId){ setVariableGetter(variables, functionId); }

    //--------------------------------------------------------------------------------------------------

    //Getters:

    public ArrayList<String> getVariables() {
        return variables;
    }

    public String getFunctionTitle() {
        return functionTitle;
    }

    public String getFunctionId() {
        return functionId;
    }

    //--------------------------------------------------------------------------------------------------

    //Setters:

    public void setVariableGetter(String functionTitle) {

        //Armazenando o título da função:

        this.functionTitle = functionTitle.replaceAll(" ", "");

        //Coletando o identificador da função:

        if(functionTitle.contains("(")){

            //Se a função tiver variáveis:
            this.functionId = functionTitle.substring(0, this.functionTitle.indexOf('('));
            this.variables = sortVariablesInReplacementOrder(getVariables(functionTitle));

        } else {

            //Se a função não tiver variáveis:
            this.functionId = functionTitle;
            this.variables = null;
        }
    }

    public void setVariableGetter(String[] variables, String functionId) {

        //Coletando o título da função:

        this.functionTitle = getFunctionTitle(variables, functionId);

        //Armazenando o identificador da função:

        this.functionId = functionId;

        //Ordenando as variáveis em ordem de substituição e armazenando-as:

        if(variables!= null && variables.length > 0){

            //Se houverem variáveis:

            this.variables = new ArrayList<>();

            for(String variable : variables)
                this.variables.add(variable);

            this.variables = sortVariablesInReplacementOrder(this.variables);

        }else{

            //Se não houverem variáveis:

            this.variables = null;
        }
    }

    //--------------------------------------------------------------------------------------------------

    //Métodos de Busca das Variáveis:

    public String getFunctionTitle(String[] variables, String functionId){

        StringBuilder functionTitle = new StringBuilder();

        if(variables==null || variables.length==0){

            functionTitle.append(functionId);

        }else if(variables.length==1){

            functionTitle.append(String.format("%s(%s)", functionId,variables[0]));

        }else{

            functionTitle.append(functionId);

            for (int index = 0 ; index < variables.length; index++) {

                if (index == 0)
                    functionTitle.append("(" + variables[index]);

                else if (index == variables.length - 1)
                    functionTitle.append(String.format(",%s)",variables[index]));

                else
                    functionTitle.append(String.format(",%s",variables[index]));
            }
        }

        return functionTitle.toString();
    }

    public ArrayList<String> getVariables(String functionTitle){

        ArrayList<String> variables = new ArrayList<>();

        String variablesBlock = functionTitle.substring(functionTitle.indexOf('('));

        String variable;
        int startIndex = 1;
        int finalIndex;

        for(int index = startIndex; index < variablesBlock.length(); index++){
            if(variablesBlock.charAt(index)==',' || variablesBlock.charAt(index)==')'){

                finalIndex = index;

                variable = variablesBlock.substring(startIndex,finalIndex);

                variables.add(variable);
                startIndex = finalIndex+1;
            }
        }

        return variables;
    }

    public ArrayList<String> sortVariablesInReplacementOrder(ArrayList<String> variables){

        ArrayList<String> variablesInReplacementOrder = new ArrayList<>();

        //Ordenando o ArrayList em ordem de substituição:

        variablesInReplacementOrder.add(0, variables.get(0));

        for (int i = 1; i < variables.size(); i++) {

            for (int j = variablesInReplacementOrder.size() - 1; j >= 0; j--) {

                if (variablesInReplacementOrder.get(j).contains(variables.get(i))
                        && !variablesInReplacementOrder.get(j).equals(variables.get(i))) {

                    //Se a posição j do vetor ordenado contem a posição i do vetor desordenado e são diferentes:
                    variablesInReplacementOrder.add(j + 1, variables.get(i));
                    break;

                } else if (j == 0) {

                    //Se nenhuma da outras alternativas se confirmarem até o fim do loop:
                    variablesInReplacementOrder.add(0, variables.get(i));
                    break;
                }
            }
        }

        return variables;
    }

    //--------------------------------------------------------------------------------------------------
}
