package ru.nsu.fit.g16201.boldyrev.Calculator.realization;

public class CalcElement {
    private String name;
    private Double value;

    public CalcElement(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    void setElementName(String name) {
        this.name = name;
    }

    void setElementValue(Double value) {
        this.value = value;
    }

    String getElementName() {
        return this.name;
    }

    Double getElementValue() { return this.value; }
}
