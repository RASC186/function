package com.fermi.MathEngine.LinearAlgebra.Algebra.Translator.Collector;

/*
    Classe responsável por coletar blocos na string, isto é, partes da função que ficam entre
    parênteses, barras de módulo, colchetes ou chaves.
*/

public class BlockCollector {

    //--------------------------------------------------------------------------------------------------

    //Variáveis:

    private String expression;

    //--------------------------------------------------------------------------------------------------

    //Construtores:

    public BlockCollector(String expression){
        setExpression(expression);
    }

    //--------------------------------------------------------------------------------------------------

    //Setters:

    public void setExpression(String expression){
        this.expression = expression;
    }

    //--------------------------------------------------------------------------------------------------

    //Métodos públicos para a coleta de indexes:

    public int getBlockStartIndex(int finalIndex){

        int startIndex = -1;

        switch (expression.charAt(finalIndex)){
            case '|':
                startIndex = getAbsStartIndex(finalIndex);
                break;
            case ')':
                startIndex = getParenthesesStartIndex(finalIndex);
                break;
            case '}':
                startIndex = getBracesStartIndex(finalIndex);
                break;
            case ']':
                startIndex = getBracketsStartIndex(finalIndex);
                break;
            case '>':
                startIndex = getAngleBracketsStartIndex(finalIndex);
                break;
            default:
                System.out.println(
                        "Error(BlockCollector)! This symbol, " + expression.charAt(finalIndex) + ", is not valid."
                );
                break;
        }

        //Retorna a função entre as barras de módulo com as barras inicial e final inclusas;
        return startIndex;
    }

    public int getBlockFinalIndex(int startIndex){

        int finalIndex = -1;

        switch (expression.charAt(startIndex)){
            case '|':
                finalIndex = getAbsFinalIndex(startIndex);
                break;
            case '(':
                finalIndex = getParenthesesFinalIndex(startIndex);
                break;
            case '{':
                finalIndex = getBracesFinalIndex(startIndex);
                break;
            case '[':
                finalIndex = getBracketsFinalIndex(startIndex);
                break;
            case '<':
                finalIndex = getAngleBracketsFinalIndex(startIndex);
                break;
            default:
                System.out.println(
                        "Error(BlockCollector)! This symbol, " + expression.charAt(startIndex) + ", is not valid."
                );
                break;
        }

        //Retorna a função entre as barras de módulo com as barras inicial e final inclusas;
        return finalIndex;
    }

    //Métodos públicos para a coleta de blocos:

    public String getBlockByStartIndex(int startIndex) {

        int finalIndex = -1;

        switch (expression.charAt(startIndex)){
            case '|':
                finalIndex = getAbsFinalIndex(startIndex);
                break;
            case '(':
                finalIndex = getParenthesesFinalIndex(startIndex);
                break;
            case '{':
                finalIndex = getBracketsFinalIndex(startIndex);
                break;
            case '[':
                finalIndex = getBracketsFinalIndex(startIndex);
                break;
            case '<':
                finalIndex = getAngleBracketsFinalIndex(startIndex);
                break;
            default:
                System.out.println(
                        "Error(BlockCollector)! This symbol," + expression.charAt(startIndex) + ", is not valid."
                );
                break;
        }

        //Retorna a função entre as barras de módulo com as barras inicial e final inclusas;
        return expression.substring(startIndex, finalIndex + 1);
    }

    public String getBlockByFinalIndex(int finalIndex) {

        int startIndex = -1;

        switch (expression.charAt(finalIndex)){
            case '|':
                startIndex = getAbsStartIndex(finalIndex);
                break;
            case ')':
                startIndex = getParenthesesStartIndex(finalIndex);
                break;
            case '}':
                startIndex = getBracesStartIndex(finalIndex);
                break;
            case ']':
                startIndex = getBracketsStartIndex(finalIndex);
                break;
            case '>':
                startIndex = getAngleBracketsStartIndex(finalIndex);
                break;
            default:
                System.out.println(
                        "Error(BlockCollector)! This symbol," + expression.charAt(finalIndex) + ", is not valid."
                );
                break;
        }

        //Retorna a função entre as barras de módulo com as barras inicial e final inclusas;
        return expression.substring(startIndex, finalIndex + 1);
    }

    //--------------------------------------------------------------------------------------------------

    //Métodos privados de coleta de índices (usando o índice inicial para encontrar o final):

    private int getAbsFinalIndex(int startIndex) {

        int openBarsCount = 0;
        int closedBarsCount = 0;
        int lastOpenBarIndex = -1;
        int lastClosedBarIndex = -1;

        for (int index = startIndex; !((lastClosedBarIndex > 0) && (closedBarsCount == openBarsCount)); index++) {

            if (expression.charAt(index) == '|') {

                if (index == startIndex) {
                    lastOpenBarIndex = index;
                    openBarsCount++;

                } else if ("+-*/%^({[,".contains(String.valueOf(expression.charAt(index - 1)))) {
                    lastOpenBarIndex = index;
                    openBarsCount++;

                } else if (expression.charAt(index - 1) == '|') {

                    if (lastOpenBarIndex == (index - 1)){
                        lastOpenBarIndex = index;
                        openBarsCount++;

                    }else{
                        lastClosedBarIndex = index;
                        closedBarsCount++;
                    }

                } else {
                    lastClosedBarIndex = index;
                    closedBarsCount++;
                }
            }
        }

        return lastClosedBarIndex;
    }

    private int getParenthesesFinalIndex(int startIndex) {

        int openParenthesisCount = 0;
        int closedParenthesisCount = 0;
        int lastClosedParenthesisIndex = -1;

        for (int index = startIndex;
             !((lastClosedParenthesisIndex > 0) && (closedParenthesisCount == openParenthesisCount)); index++) {

            switch (expression.charAt(index)) {
                case '(':
                    openParenthesisCount++;
                    break;
                case ')':
                    lastClosedParenthesisIndex = index;
                    closedParenthesisCount++;
                    break;
            }
        }

        return lastClosedParenthesisIndex;
    }

    private int getBracesFinalIndex(int startIndex) {

        int openBracesCount = 0;
        int closedBracesCount = 0;
        int lastClosedBraceIndex = -1;

        for (int index = startIndex;
             !((lastClosedBraceIndex > 0) && (closedBracesCount == openBracesCount)); index++) {
            switch (expression.charAt(index)) {
                case '{':
                    openBracesCount++;
                    break;
                case '}':
                    lastClosedBraceIndex = index;
                    closedBracesCount++;
                    break;
            }
        }

        return lastClosedBraceIndex;
    }

    private int getBracketsFinalIndex(int startIndex) {

        int openBracketsCount = 0;
        int closedBracketsCount = 0;
        int lastClosedBracketIndex = -1;

        for (int index = startIndex;
             !((lastClosedBracketIndex > 0) && (closedBracketsCount == openBracketsCount)); index++) {
            switch (expression.charAt(index)) {
                case '[':
                    openBracketsCount++;
                    break;
                case ']':
                    lastClosedBracketIndex = index;
                    closedBracketsCount++;
                    break;
            }
        }

        return lastClosedBracketIndex;
    }

    private int getAngleBracketsFinalIndex(int startIndex) {

        int openAngleBracketsCount = 0;
        int closedAngleBracketsCount = 0;
        int lastClosedAngleBracketIndex = -1;

        for (int index = startIndex;
             !((lastClosedAngleBracketIndex > 0) && (closedAngleBracketsCount == openAngleBracketsCount)); index++) {
            switch (expression.charAt(index)) {
                case '<':
                    openAngleBracketsCount++;
                    break;
                case '>':
                    lastClosedAngleBracketIndex = index;
                    closedAngleBracketsCount++;
                    break;
            }
        }

        return lastClosedAngleBracketIndex;
    }

    //Métodos privados de coleta de índices (usando o índice final para encontrar o inicial):

    private int getAbsStartIndex(int finalIndex) {

        int openBarsCount = 0;
        int closedBarsCount = 0;
        int lastOpenBarIndex = -1;
        int lastClosedBarIndex = -1;

        for (int index = finalIndex;
             (index >= 0) && !((lastOpenBarIndex > 0) && (closedBarsCount == openBarsCount));
             index--) {

            if (expression.charAt(index) == '|') {

                if (index == finalIndex) {
                    lastClosedBarIndex = index;
                    closedBarsCount++;

                } else if (".0123456789)}]".contains(String.valueOf(expression.charAt(index - 1)))) {
                    lastClosedBarIndex = index;
                    closedBarsCount++;

                } else if (expression.charAt(index + 1) == '|') {

                    if (lastClosedBarIndex == (index + 1)){
                        lastClosedBarIndex = index;
                        closedBarsCount++;

                    }else{
                        lastOpenBarIndex = index;
                        openBarsCount++;
                    }

                } else {
                    lastOpenBarIndex = index;
                    openBarsCount++;
                }
            }
        }

        return lastOpenBarIndex;
    }

    private int getParenthesesStartIndex(int finalIndex) {

        int openParenthesesCount = 0;
        int closedParenthesesCount = 0;
        int lastOpenParenthesisIndex = -1;

        for (int index = finalIndex;
             (index >=0) && !((lastOpenParenthesisIndex > 0) && (closedParenthesesCount == openParenthesesCount));
             index--) {

            switch (expression.charAt(index)) {
                case '(':
                    lastOpenParenthesisIndex = index;
                    openParenthesesCount++;
                    break;
                case ')':
                    closedParenthesesCount++;
                    break;
            }
        }

        return lastOpenParenthesisIndex;
    }

    private int getBracesStartIndex(int finalIndex) {

        int openBracesCount = 0;
        int closedBracesCount = 0;
        int lastOpenBraceIndex = -1;

        for (int index = finalIndex;
             (index >=0) && !((lastOpenBraceIndex > 0) && (closedBracesCount == openBracesCount));
             index--) {

            switch (expression.charAt(index)) {
                case '{':
                    lastOpenBraceIndex = index;
                    openBracesCount++;
                    break;
                case '}':
                    closedBracesCount++;
                    break;
            }
        }

        return lastOpenBraceIndex;
    }

    private int getBracketsStartIndex(int finalIndex) {

        int openBracketsCount = 0;
        int closedBracketsCount = 0;
        int lastOpenBracketIndex = -1;

        for (int index = finalIndex;
             (index >=0) && !((lastOpenBracketIndex > 0) && (closedBracketsCount == openBracketsCount));
             index--) {

            switch (expression.charAt(index)) {
                case '[':
                    lastOpenBracketIndex = index;
                    openBracketsCount++;
                    break;
                case ']':
                    closedBracketsCount++;
                    break;
            }
        }

        return lastOpenBracketIndex;
    }

    private int getAngleBracketsStartIndex(int finalIndex) {

        int openAngleBracketsCount = 0;
        int closedAngleBracketsCount = 0;
        int lastOpenAngleBracketIndex = -1;

        for (int index = finalIndex;
             (index >=0) && !((lastOpenAngleBracketIndex > 0) && (closedAngleBracketsCount == openAngleBracketsCount));
             index--) {

            switch (expression.charAt(index)) {
                case '<':
                    lastOpenAngleBracketIndex = index;
                    openAngleBracketsCount++;
                    break;
                case '>':
                    closedAngleBracketsCount++;
                    break;
            }
        }

        return lastOpenAngleBracketIndex;
    }

    //--------------------------------------------------------------------------------------------------
}
