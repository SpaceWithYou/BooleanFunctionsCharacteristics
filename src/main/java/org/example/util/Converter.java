package org.example.util;

import org.example.functions.BooleanFunction;
import org.example.functions.algos.Algorithms;

import java.util.ArrayList;
import java.util.Collections;
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
     * это список номеров переменных в одном из конъюнктов, причём переменная
     * отрицательная, если она входит с отрицанием
     * */
    public static List<List<Integer>> valueVectorToDNF(BooleanFunction function) {
        var result = new ArrayList<List<Integer>>();
        var bits = function.getBits();
        var variablesCount = function.getVariablesCount();

        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                var disjunct = new ArrayList<Integer>();
                for (int j = variablesCount - 1; j >= 0; j--) {
                    if ((~i & (1 << j)) != 0) {
                        disjunct.add(variablesCount - j);
                    } else {
                        disjunct.add(-(variablesCount - j));
                    }
                }
                result.add(disjunct);
            }
        }

        return result;
    }

    /**
     * Конвертирует представление булевой функции в виде массива битов в КНФ
     * @param function Bitset, представляющий вектор значений булевой функции
     * @return Список списков, представляющий КНФ, где каждый элемент списка -
     * это список номеров переменных в одном из конъюнктов, причём переменная
     * отрицательная, если она входит с отрицанием
     * */
    public static List<List<Integer>> valueVectorToCNF(BooleanFunction function) {
        var result = new ArrayList<List<Integer>>();
        var bits = function.getBits();
        var variablesCount = function.getVariablesCount();

        for (int i = 0; i < bits.length(); i++) {
            if (!bits.get(i)) {
                var disjunct = new ArrayList<Integer>();
                for (int j = variablesCount - 1; j >= 0; j--) {
                    if ((~i & (1 << j)) != 0) {
                        disjunct.add(-(variablesCount - j));
                    } else {
                        disjunct.add(variablesCount - j);
                    }
                }
                result.add(disjunct);
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
//        var moebiusTransform = Algorithms.moebiusTransform(function);
//        var variablesCount = function.getVariablesCount();
//
//        var result = new ArrayList<List<Integer>>();
//        for (int i = 0; i < moebiusTransform.length(); i++) {
//            if (moebiusTransform.get(i)) {
//                var variables = new ArrayList<Integer>();
//                for (int j = 0; j < variablesCount; j++) {
//                    if ((i & (1 << j)) != 0) {
//                        variables.add(j + 1);
//                    }
//                }
//                result.add(variables);
//            }
//        }

        // Преобразуем в BitSet коэффициентов (сдвинутый на +1)
        var coeffs = Algorithms.moebiusTransform(function);
        int n = function.getVariablesCount();

        List<List<Integer>> result = new ArrayList<>();
        // пробегаем по всем «1»-битам в BitSet
        for (int bit = coeffs.nextSetBit(0); bit >= 0; bit = coeffs.nextSetBit(bit + 1)) {
            int idx = bit - 1;
            // idx == -1  → пустой список  → константа 1
            if (idx < 0) {
                result.add(Collections.emptyList());
            } else {
                List<Integer> monomial = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (((idx >> j) & 1) != 0) {
                        monomial.add(j + 1);
                    }
                }
                result.add(monomial);
            }
        }
        return result;
    }
}
