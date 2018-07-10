package ru.nsu.fit.g16201.boldyrev.Calculator.operations;

import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.StackAmount;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Operation;

import java.util.ArrayList;

public class MINUS implements Operation {
    @Override
    public void method(Context context, ArrayList<String> args) throws StackAmount {
        if (context.getStackSize() < 2) {
            throw new StackAmount("Error: not enough parameters in stack, terminating the program.");
        }

        Double element1;
        Double element2;

        element1 = context.popContextStack();
        element2 = context.popContextStack();
        element1 -= element2;
        context.pushContextStack(element1);
    }
}
