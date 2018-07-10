package ru.nsu.fit.g16201.boldyrev.Calculator.operations;

import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Operation;

import java.util.ArrayList;

public class PUSH implements Operation {
    @Override
    public void method(Context context, ArrayList<String> args) {
        String pushedName = args.remove(0);
        Double pushedValue = context.getContextParameters(pushedName);
        context.pushContextStack(pushedValue);
    }
}