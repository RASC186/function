package com.fermi;

import com.fermi.MathEngine.Derivator;
import com.fermi.MathEngine.LinearAlgebra.Algebra.Function;
import com.fermi.MathEngine.LinearAlgebra.Matrix;
import com.fermi.MathEngine.LinearAlgebra.MatrixExpression;

public class Main {

    public static void main(String[] args) {

        double[][] result;

        Function function = new Function();
        Matrix matrix = new Matrix();
        MatrixExpression matrixExpression;

        //* Seção de teste das funções numéricas:

        System.out.println(
                "----------------------------------------------------------------------------------------------------\n"
        );

        System.out.println("\tSeção de Testes da Classe de Expressões Numéricas e Algébricas (Function)\n");

        function.setFunction("f = sin(#pi) - cos(#pi) + tan(#pi) + " +
                "sec(#pi) - cossec(#pi/2) - cotan(#pi/2) + " +
                "asin(0) + acos(1) + atan(0)");
        System.out.println(function);
        System.out.println("Result: " + function.calculate());

        System.out.println();

        function.setFunction("h = -|1+|-2.0|-1|^-||1.0+2|-2|^(4.5+5.5)^+2^3.5 + 5.0^2*|-1/25|");
        System.out.println(function);
        System.out.println("Result: " + function.calculate());

        System.out.println();

        String f = "g = ln((#e)^2) + log(#e,ln(#e)) - log(2, log(4,|-|-16|+sin(#pi)*#e|))";
        function.setFunction(f);
        System.out.println(function);
        System.out.println("Result: " + function.calculate());

        System.out.println();

        function.setFunction("L = -1+2*3/2+(45-5)*2");
        System.out.println(function);
        System.out.println("Result: " + function.calculate());

        System.out.println();

        //*/

        //* Seção de teste das matrizes:

        System.out.println(
                "----------------------------------------------------------------------------------------------------\n"
        );

        System.out.println("\tSeção de Testes da Classe de Matrizes (Matrix)\n");

        String m1 = "[18.0_0.0;27.0_207.0;-0.0036661175904728355_405.0]";
        matrix = new Matrix(null, "M1", m1);
        result = matrix.calculate(null, null);
        System.out.println("M1: " + m1 + " = " + matrix.matrix2String(result));

        String m2 = "[ x _ y ; x^2 _ y^2 ]";
        matrix = new Matrix(new String[]{"x","y"}, "M2", m2);
        result = matrix.calculate(new String[]{"x","y"}, new double[]{2,3});
        System.out.println("M2: " + m2 + " = " + matrix.matrix2String(result));

        String m3 = "M3(x,y,z) = [ 1*x _ 2*y _ 3*z ; 3*x^2 _ 2*y^2  _ 1*z^2]";
        matrix = new Matrix(m3);
        result = matrix.calculate(new String[]{"x","y","z"}, new double[]{1,2,3});
        System.out.println("M3: " + m3 + " = " + matrix.matrix2String(result));


        System.out.println("\n");

        //*/

        //* Seção de teste das funções matriciais:

        System.out.println(
                "----------------------------------------------------------------------------------------------------\n"
        );

        System.out.println("\tSeção de Testes da Classe de Expressões Matriciais (MatrixExpression)\n");

        String me1 = "3*(inv([2*x_3*y;23_45]+2*transp([2*x_3*y;23_45]))+[x_y;x^2_y^2]^2)";
        matrixExpression = new MatrixExpression(new String[]{"y","x"}, "ME1", me1);
        result = matrixExpression.calculate(new String[]{"y","x"}, new double[]{1,1});
        System.out.println("ME1: " + me1 + " = " + matrix.matrix2String(result));

        String me2 = "ME2(x,y) = 3*([2*x_3*y;23_45])^-1*log(#e,#e) + 2*sin(#pi/2)*transp([x_y;x^2_y^2])^2^2";
        matrixExpression = new MatrixExpression(me2);
        result = matrixExpression.calculate(new String[]{"y","x"}, new double[]{1,1});
        System.out.println("ME2: " + me2 + " = " + matrix.matrix2String(result));

        String me3 = "ME3(x,y) = 3*x + (4/2) + (sin(#pi/2)) + (2*y)^2^(2+(cos(#pi/2))*1)";
        matrixExpression = new MatrixExpression(me3);
        result = matrixExpression.calculate(new String[]{"y","x"}, new double[]{1,1});
        System.out.println("ME3: " + me3 + " = " + matrix.matrix2String(result));

        String me4 = "ME4 = inv([6_2_12_2;0_0_-15_2;0_2_-2_1;0_-5.67_-2_4.33])";//Entender a natureza do erro
        matrixExpression = new MatrixExpression(me4);
        result = matrixExpression.calculate(null,null);
        System.out.println("ME4: " + me4 + " = " + matrix.matrix2String(result));

        System.out.println();

        //*/

        //* Seção de teste para funções algébricas e derivação numérica:

        System.out.println(
                "----------------------------------------------------------------------------------------------------\n"
        );

        System.out.println("\tSeção de Testes da Classe de Derivação (Derivator)\n");
        double x = 0;
        function.setFunction("f(x)=sin(x)+|-1|");//Buscar entender porque quando x=0 as derivadas divergem
        System.out.println(function);
        System.out.println("Result..........: " + function.calculate(new String[]{"x"},new double[]{x}));
        System.out.println("1# Derivative...: " +
                new Derivator(function).firstDerivative(new String[]{"x"},new double[]{x},"x"));
        System.out.println("2# Derivative...: " +
                new Derivator(function).secondDerivative(new String[]{"x"},new double[]{x},"x"));

        System.out.println();

        function.setFunction(new String[]{"a","b"},"t", "a^10+b^-(1)");
        System.out.println(function);
        System.out.println("Result..........: " + function.calculate(new String[]{"a","b"},new double[]{2,2}));
        System.out.println("1# Derivative...: " +
                new Derivator(function).firstDerivative(new String[]{"a","b"},new double[]{2,2},"b"));
        System.out.println("2# Derivative...: " +
                new Derivator(function).secondDerivative(new String[]{"a","b"},new double[]{2,2},"b"));

        System.out.println();

        function.setFunction("w(y,z,x) = x^2 + 2*y*x + z");
        System.out.println(function);
        System.out.println("Result..........: " + function.calculate(new String[]{"x","y","z"},new double[]{2,1,4}));
        System.out.println("1# Derivative...: " +
                new Derivator(function).firstDerivative(new String[]{"x","y","z"},new double[]{2,1,4},"x"));
        System.out.println("2# Derivative...: " +
                new Derivator(function).secondDerivative(new String[]{"x","y","z"},new double[]{2,1,4},"x"));
        System.out.println("Mixed Derivative: " +
                new Derivator(function).mixedDerivative(
                        new String[]{"x","y","z"},new double[]{1,2,1},new String[]{"x","y"}
                        )
        );
        System.out.println("Hessian Matrix..: " +
                new Matrix("M",
                        new Derivator(function).hessian(
                                new String[]{"x","y","z"},
                                new double[]{1,2,1}
                                )
                )
        );

        System.out.println();

        //*/
    }
}
