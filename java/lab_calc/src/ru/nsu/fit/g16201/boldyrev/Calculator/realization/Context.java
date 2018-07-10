package ru.nsu.fit.g16201.boldyrev.Calculator.realization;

import java.util.ArrayDeque;
import java.util.HashMap;

public class Context {
    private ArrayDeque<Double> contextStack;
    private HashMap<String, Double> contextParameters;

    public Context() {
        contextStack = new ArrayDeque<Double>();
        contextParameters = new HashMap<String, Double>();
    }

    public void pushContextStack(Double element) {
        contextStack.addFirst(element);
    }

    public Double popContextStack() {
        return contextStack.pollFirst();
    }

    public Double topStackValue() {
        return contextStack.getFirst();
    }

    public void setContextParameters(CalcElement element) {
        contextParameters.put(element.getElementName(), element.getElementValue());
    }

    public Double getContextParameters(String elementName) {
        return contextParameters.get(elementName);
    }

    public void cleanStack() {
        while (contextStack.size() > 0) {
            popContextStack();
        }
    }

    public int getStackSize() {
        return contextStack.size();
    }

    public int getParamsSize() {
        return contextParameters.size();
    }
}
