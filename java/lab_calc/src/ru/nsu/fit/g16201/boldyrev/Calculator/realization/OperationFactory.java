package ru.nsu.fit.g16201.boldyrev.Calculator.realization;

import java.io.IOException;
import java.util.Properties;

public class OperationFactory {
    private static OperationFactory instance ;
    private Properties prop = new Properties();

    private OperationFactory() throws IOException {
        prop.load(OperationFactory.class.getResourceAsStream("config.properties"));
    }

    public static OperationFactory getInstance() throws IOException {
        if(instance == null)
            instance = new OperationFactory();
        return instance;
    }

    public Operation getCommand(String name) throws Exception {
        if(!prop.containsKey(name)) {
            throw new Exception("Error");
        }
        return (Operation) Class.forName((String)prop.get(name)).newInstance();
    }


}