package org.example.util;

import lombok.RequiredArgsConstructor;
import org.example.functions.BooleanFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.regex.Pattern;

/**
 * Парсер для ввода булевых функций
 * */
@RequiredArgsConstructor
public class Parser {
    private final BufferedReader reader;

    private static final String LINE_PATTERN = "^[01]+$";

    /**
     * Читает строку, котораря задаёт булеву функцию в виде вектора.
     * @return Bitset, представляющий булеву функцию
     * @throws IOException если произошло исключение IO
     * @throws IllegalArgumentException если строка не представляет булеву функцию
     * */
    public BooleanFunction parse() throws IOException, IllegalArgumentException {
        var line = reader.readLine();

        int number = returnVariableNumber(line);
        if(!Pattern.matches(LINE_PATTERN, line) && number == -1) {
            throw new IllegalArgumentException("Wrong line!");
        }

        return new BooleanFunction(BitSet.valueOf(line.getBytes()), number);
    }

    /**
     * Возвращает количество переменных для строки, представляющей
     * булеву функцию
     * @return количество переменных, либо -1, если строка некорректна
     * */
    private int returnVariableNumber(String line) {
        var lineLength = line.length();
        var numberOfVariables = 1;

        //Проверяем что длинна строки - степень двойки
        while (lineLength > 0) {
            if(lineLength == 1) {
                break;
            } else if (lineLength % 2 == 1) {
                return -1;
            }
            lineLength /= 2;
            numberOfVariables++;
        }
        return numberOfVariables;
    }
}
