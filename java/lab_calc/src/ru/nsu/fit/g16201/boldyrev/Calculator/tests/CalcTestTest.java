package ru.nsu.fit.g16201.boldyrev.Calculator.tests;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.DividedByZero;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.StackAmount;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.TooManyArgs;
import ru.nsu.fit.g16201.boldyrev.Calculator.operations.*;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.CalcElement;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalcTestTest {
    Context context = new Context();
    ArrayList<String> params = new ArrayList<String>();

    @After
    public void clean() {
        context.cleanStack();
        params.clear();
    }

    @Test
    public void testSum1() {
        Double a = 4.0;
        Double b = 2.0;
        context.pushContextStack(a);
        context.pushContextStack(b);

        PLUS plus = new PLUS();
        try {
            plus.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)6.0, result);
    }

    @Test
    public void testSum2() {
        Double a = -43.86;
        Double b = 0.001;
        context.pushContextStack(a);
        context.pushContextStack(b);

        PLUS plus = new PLUS();
        try {
            plus.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)(-43.859), result);
    }

    @Test
    public void testMinus1() {
        Double a = -5.0;
        Double b = 3.0;
        context.pushContextStack(a);
        context.pushContextStack(b);

        MINUS minus = new MINUS();
        try {
            minus.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)8.0, result);
    }

    @Test
    public void testMinus2() {
        Double b = -5.0;
        Double a = 3.0;
        context.pushContextStack(a);
        context.pushContextStack(b);

        MINUS minus = new MINUS();
        try {
            minus.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)(-8.0), result);
    }

    @Test
    public void testMulti1() {
        Double b = 0.0;
        Double a = 3.0;
        context.pushContextStack(a);
        context.pushContextStack(b);

        MULTIPLICATE multi = new MULTIPLICATE();
        try {
            multi.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)0.0, result);
    }

    @Test
    public void testMulti2() {
        Double b = 428.8;
        Double a = -22.2;
        context.pushContextStack(a);
        context.pushContextStack(b);

        MULTIPLICATE multi = new MULTIPLICATE();
        try {
            multi.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)(-9519.36), result);
    }

    @Test
    public void testDivide1() {
        Double b = 6.0;
        Double a = 3.0;
        context.pushContextStack(a);
        context.pushContextStack(b);

        DIVIDE divide = new DIVIDE();
        try {
            divide.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        } catch(DividedByZero dividedByZero) {
            dividedByZero.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)2.0, result);
    }

    @Test
    public void testDivide2() {
        Double a = -3.0;
        Double b = 9.0;
        context.pushContextStack(a);
        context.pushContextStack(b);

        DIVIDE divide = new DIVIDE();
        try {
            divide.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        } catch(DividedByZero dividedByZero) {
            dividedByZero.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)(-3.0), result);
    }

    @Test
    public void testSqrt1() {
        Double a = 9.0;

        context.pushContextStack(a);

        SQRT sqrt = new SQRT();
        try {
            sqrt.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        Double result = context.popContextStack();
        assertEquals((Double)(3.0), result);
    }

    @Test
    public void testDefine1() {
        String name = "a";
        String value = "1";
        params.add(name);
        params.add(value);

        DEFINE define = new DEFINE();
        try {
            define.method(context, params);
        } catch (TooManyArgs tooManyArgs) {
            tooManyArgs.printStackTrace();
        }

        assertEquals(1, context.getParamsSize());
    }

    @Test
    public void testPop1() {
        Double a = 1.0;
        Double b = 2.0;
        Double c = 3.0;

        context.pushContextStack(a);
        context.pushContextStack(b);
        context.pushContextStack(c);

        POP pop = new POP();
        try {
            pop.method(context, params);
        } catch(StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }

        assertEquals(2, context.getStackSize());
    }

    @Test
    public void testPush1() {
        Double a = 1.0;
        Double b = 2.0;
        String name = "c";
        Double value = 3.0;
        CalcElement element = new CalcElement(name, value);

        context.pushContextStack(a);
        context.pushContextStack(b);

        PUSH push = new PUSH();
        context.setContextParameters(element);
        params.add(name);
        push.method(context, params);

        assertEquals(3, context.getStackSize());
    }

    @Test
    public void testPrint1() {
        Double a = 1.0;
        String result;

        context.pushContextStack(a);

        PRINT print = new PRINT();

        ByteArrayOutputStream newOutput = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(newOutput);
        PrintStream oldOutput = System.out;
        System.setOut(ps);
        try {
            print.method(context, params);
        } catch (StackAmount stackAmount) {
            stackAmount.printStackTrace();
        }
        System.out.flush();
        System.setOut(oldOutput);
        assertEquals("1.0\n", newOutput.toString());
    }

    public static void main(String[] args) throws Exception {
        JUnitCore runner = new JUnitCore();
        Result result = runner.run(CalcTestTest.class);
        System.out.println("run tests: " + result.getRunCount());
        System.out.println("failed tests: " + result.getFailureCount());
        System.out.println("ignored tests: " + result.getIgnoreCount());
        System.out.println("success: " + result.wasSuccessful());
    }


}