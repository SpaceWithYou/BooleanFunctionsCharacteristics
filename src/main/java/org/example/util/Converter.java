package org.example.util;

import org.example.functions.BooleanFunction;
import org.example.functions.algos.Algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Конвертирует булеву функцию из одного представления в другое
 * */
public class Converter {

    private Converter() {
        // private constructor to prevent instantiation
    }
    /**
     * Конвертирует представление булевой функции в виде массива битов в ДНФ
     * @param function Bitset, представляющий вектор значений булевой функции
     * @return Список списков, представляющий ДНФ, где каждый элемент списка -
     * это список номеров переменных в одном из конъюнктов
     * */
    public static List<List<Integer>> valueVectorToDNF(BooleanFunction function) {
        var result = new ArrayList<List<Integer>>();
        var bits = function.getBits();
        var variablesCount = function.getVariablesCount();

        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                result.add(getVariablesNumbers(variablesCount, i));
            }
        }

        return result;
    }

    /**
     * Конвертирует представление булевой функции в виде массива битов в КНФ
     * @param function Bitset, представляющий вектор значений булевой функции
     * @return Список списков, представляющий ДНФ, где каждый элемент списка -
     * это список номеров переменных в одном из конъюнктов
     * */
    public static List<List<Integer>> valueVectorToCNF(BooleanFunction function) {
        var result = new ArrayList<List<Integer>>();
        var bits = function.getBits();
        var variablesCount = function.getVariablesCount();

        for (int i = 0; i < bits.length(); i++) {
            if (!bits.get(i)) {
                result.add(getVariablesNumbers(variablesCount, i));
            }
        }

        return result;
    }

    /**
     * Конвертирует представление булевой функции в виде массива битов в АНФ
     * @param function Bitset, представляющий вектор значений булевой функции
     * @return Список списков, представляющий АНФ, где каждый элемент списка -
     * это список номеров переменных в одном из дизъюнктов
     * */
    public static List<List<Integer>> valueVectorToANF(BooleanFunction function) {
        var moebiusTransform = Algorithms.moebiusTransform(function);

        var result = new ArrayList<List<Integer>>();
        for (int i = 0; i < moebiusTransform.length(); i++) {
            if (moebiusTransform.get(i)) {
                result.add(getVariablesNumbers(function.getVariablesCount(), i));
            }
        }
        return result;
    }

    /**
     * Получение номеров переменных в таблице истинности
     * */
    private static List<Integer> getVariablesNumbers(int variablesCount, int i) {
        var disjunct = new ArrayList<Integer>();
        for (int j = 0; i < variablesCount; i++) {
            if ((i & (1 << j)) == 1) {
                disjunct.add(j);
            }
        }
        return disjunct;
    }
}
