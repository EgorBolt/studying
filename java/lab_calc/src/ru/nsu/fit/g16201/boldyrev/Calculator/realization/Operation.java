package ru.nsu.fit.g16201.boldyrev.Calculator.realization;

import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.DividedByZero;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.NoSuchCommand;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.StackAmount;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.TooManyArgs;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;

import java.util.ArrayList;

public interface Operation {
    public void method(Context context, ArrayList<String> args) throws TooManyArgs, NoSuchCommand, DividedByZero, StackAmount;
}
