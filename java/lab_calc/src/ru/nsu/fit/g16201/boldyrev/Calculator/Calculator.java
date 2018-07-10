package ru.nsu.fit.g16201.boldyrev.Calculator;

import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.DividedByZero;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.StackAmount;
import ru.nsu.fit.g16201.boldyrev.Calculator.exceptions.TooManyArgs;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Context;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.Operation;
import ru.nsu.fit.g16201.boldyrev.Calculator.realization.OperationFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator {
    public void run(InputStream in) {
        String classname;
        String[] parsedLine;
        ArrayList<String> params = new ArrayList<String>();
        Context context = new Context();
        String line;
        int length;
        BufferedReader reader = null;
        Logger logger = Logger.getLogger(Calculator.class.getName());
        try {
            FileHandler handler = new FileHandler("logfile.txt");
            logger.addHandler(handler);
        } catch (IOException ioError) {
            ioError.printStackTrace();
        }

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            while (((line = reader.readLine()) != null) && (!line.equals("END"))) {
                parsedLine = line.split(" ");
                if (parsedLine[0] != "#") {
                    length = parsedLine.length;
                    classname = parsedLine[0];
                    for (int index = 1; index < length; index++) {
                        params.add(parsedLine[index]);
                    }
                    try {
                        Operation operation = OperationFactory.getInstance().getCommand(classname);
                        operation.method(context, params);
                    } catch (TooManyArgs tooManyArgs) {
                        tooManyArgs.printStackTrace();
                        logger.log(Level.WARNING, "Error: too many arguments in command " + "DEFINE" + ", the rest will be deleted.");
                    }
                }
           }
        } catch (DividedByZero dividedByZero) {
            dividedByZero.printStackTrace();
            logger.log(Level.SEVERE, "Dividing by zero, terminating the program.");
        } catch (StackAmount stackAmount) {
            stackAmount.printStackTrace();
            logger.log(Level.SEVERE, "Error: not enough parameters in stack, terminating the program.");
        } catch (IOException errIO){
            System.err.println("It's an IO problem.");
            logger.log(Level.SEVERE, "Something wrong with I/O.");
        } catch (Exception errOtherProblem) {
            System.err.println("There's a random problem.");
            logger.log(Level.SEVERE, "Something wrong happened!");
        }
    }
}
