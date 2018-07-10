package ru.nsu.fit.g16201.boldyrev.Calculator.operations;

import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.StackAmount;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Operation;

import java.util.ArrayList;

public class POP implements Operation {
    @Override
    public void method(Context context, ArrayList<String> args) throws StackAmount {
        if (context.getStackSize() < 1) {
            throw new StackAmount("Error: not enough parameters in stack, terminating the program.");
        }
        context.popContextStack();
    }
}
