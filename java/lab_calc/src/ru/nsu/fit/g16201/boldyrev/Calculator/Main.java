package ru.nsu.fit.g16201.boldyrev.Calculator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        InputStream in;

        try {
            if (args.length > 0) {
                in = new FileInputStream(args[0]);
            } else {
                in = System.in;
            }
            Calculator calc = new Calculator();
            calc.run(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}

/*TODO
    1. Сделать класс OperationException, от которого будут наследоваться все остальные мои классы-исключения
        Все выкидываемые исключения должны обрабатываться в Calculator, а не внутри класса-операции
        Добавить исключение, проверяющее количество элементов на стеке
    2. Фабрика сделана, мягко говоря, неправильно. Есть фотография (и код, надеюсь), как фабрика должна быть выполнена на самом деле.
        Не должно быть creators, всё можно сделать через ReflectionAPI (каким-то образом), смотри фотографию
    3. Прочитай про наследование
    4. System.setOut() для теста + printStream: к нему надо сделать наследника
*/
