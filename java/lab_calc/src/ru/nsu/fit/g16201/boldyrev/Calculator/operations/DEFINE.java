package ru.nsu.fit.g16201.boldyrev.Calculator.operations;

import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.TooManyArgs;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.CalcElement;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Operation;

import java.util.ArrayList;

public class DEFINE implements Operation {
    @Override
    public void method(Context context, ArrayList<String> args) throws TooManyArgs {
        String argName = args.remove(0);
        String buffer = args.remove(0);
        Double argValue = Double.parseDouble(buffer);

        CalcElement element = new CalcElement(argName, argValue);
        context.setContextParameters(element);
        if (args.size() > 0) {
            while (args.size() > 0) args.remove(0);
            throw new TooManyArgs("Error: too many arguments in command " + this.getClass().getSimpleName() + ", the rest will be deleted.");
        }
     }
}

