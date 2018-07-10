package ru.nsu.fit.g16201.boldyrev.Calculator.operations;

import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.DividedByZero;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.StackAmount;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Operation;

import java.util.ArrayList;

public class DIVIDE implements Operation {
    @Override
    public void method(Context context, ArrayList<String> args) throws StackAmount, DividedByZero {
        if (context.getStackSize() < 2) {
            throw new StackAmount("Error: not enough parameters in stack, terminating the program.");
        }
        Double element1;
        Double element2;
        element1 = context.popContextStack();
        element2 = context.popContextStack();
        if (element2 == 0) {
            throw new DividedByZero("Dividing by zero, terminating the program.");
        }
        element1 /= element2;
        context.pushContextStack(element1);

    }
}

