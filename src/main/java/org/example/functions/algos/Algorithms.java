package org.example.functions.algos;

import org.example.functions.BooleanFunction;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * Основные алгоритмы для работы с булевыми функциями
 */
public class Algorithms {

    private Algorithms() {
        // private constructor to prevent instantiation
    }

    /**
     * Вычисляет вес Хэмминга булевой функции
     * @param function булевая функция
     * @return вес функции
     * */
    public static int hammingWeight(BooleanFunction function) {
        return function.getBits().cardinality();
    }

    /**
     * Вычисляет преобразование Мёбиуса булевой функции
     * @param function булева функция
     * @return преобразование Мёбиуса булевой функции
     * @see "Algorithmic Cryptanalysis Antoine Joux, Algorithm 9.6"
     * */
    public static BitSet moebiusTransform(BooleanFunction function) {
        var n = function.getVariablesCount();
        int size, position;

        var result = BitSet.valueOf(function.getBits().toLongArray());
        for (int i = 0; i < n; i++) {
            size = 1 << i;
            position = 0;
            while (position < (1 << (n - 1))) {
                for (int j = 0; j < size; j++) {
                    if(result.get(position + j) ^ result.get(position + size + j)) {
                        result.set(position + size + j);
                    }
                }

                position += size << 1;
            }
        }

        return result;
    }

    /**
     * Вычисляет преобразование Уолша булевой функции
     * @param function булева функция
     * @return массив с коэффициентами преобразования Уолша булевой функции
     * @see "Algorithmic Cryptanalysis Antoine Joux, Algorithm 9.3"
     * */
    public static int[] walshTransform(BooleanFunction function) {
        var n = function.getVariablesCount();
        int size, position, sum, diff;

        var result = new int[n];
        for (int i = 0; i < (1 << (n - 1)); i++) {
            result[i] = function.getBit(i) ? -1 : 1;
        }

        for (int i = 0; i < n; i++) {
            size = 1 << i;
            position = 0;
            while (position < (1 << (n - 1))) {
                for (int j = 0; j < size; j++) {
                    sum = result[position + j] + result[position + size + j];
                    diff = result[position + j] - result[position + size + j];
                    result[position + j] = sum;
                    result[position + size + j] = diff;
                }
                position += size << 1;
            }
        }

        return result;
    }

    /**
     * Вычисляет дифференциальные характеристики булевой функции (DDT)
     * @param function булева функция
     * @return матрица с дифференциальными разностями (DDT таблица)
     * @see "Algorithmic Cryptanalysis Antoine Joux, Algorithm 9.1"
     * */
    public static int[][] ddtTable(BooleanFunction function) {
        int inputSize = 1 << function.getVariablesCount(), xPrime;
        var diffTable = new int[inputSize][2];

        for (int deltaX = 0; deltaX < inputSize; deltaX++) {
            for (int x = 0; x < inputSize; x++) {
                xPrime = x ^ deltaX;
//                if (xPrime >= inputSize) {
//                    continue;
//                }

                diffTable[deltaX][(function.getBit(x) ^ function.getBit(xPrime)) ? 1 : 0]++;
            }
        }

        return diffTable;
    }

    /**
     * Вычисляет обратное преобразование Уолша булевой функции
     * @param function булева функция
     * @return массив с коэффициентами преобразования Уолша булевой функции
     * @see "Algorithmic Cryptanalysis Antoine Joux, Algorithm 9.4"
     * */
    public static int[] inverseWalshTransform(BooleanFunction function) {
        var n = function.getVariablesCount();
        int size, position, sum, diff;

        var result = new int[n];
        for (int i = 0; i < (1 << (n - 1)); i++) {
            result[i] = function.getBit(i) ? -1 : 1;
        }

        for (int i = 0; i < n; i++) {
            size = 1 << i;
            position = 0;
            while (position < (1 << (n - 1))) {
                for (int j = 0; j < size; j++) {
                    sum = result[position + j] + result[position + size + j];
                    diff = result[position + j] - result[position + size + j];
                    result[position + j] = sum / 2;
                    result[position + size + j] = diff / 2;
                }
                position += size << 1;
            }
        }

        return result;
    }

    /**
     * Вычисляет алгебраическую степень булевой функции
     * @param anf список списков, представляющий АНФ, где каждый элемент списка -
     * это список номеров переменных в одном из дизъюнктов
     * @return алгебраическая степень функции
     * @see org.example.util.Converter#valueVectorToANF
     * */
    public static int algebraicDegree(List<List<Integer>> anf) {
        return anf.stream()
                .map(List::size)
                .max(Integer::compareTo)
                .orElse(0);
    }

    /**
     * Проверяет, является ли функция аффинной
     * @param anf список списков, представляющий АНФ, где каждый элемент списка -
     * это список номеров переменных в одном из дизъюнктов
     * @return true, если функция аффинная, иначе false
     * @see org.example.util.Converter#valueVectorToANF
     * */
    public static boolean isAffine(List<List<Integer>> anf) {
        return anf.stream()
                .map(List::size)
                .max(Integer::compareTo)
                .orElse(0) == 1;
    }

    /**
     * Вычисляет нелинейность булевой функции
     * @param walshSpectrum массив с коэффициентами преобразования Уолша булевой функции
     * @return нелинейность функции
     * @see org.example.functions.algos.Algorithms#walshTransform
     * */
    public static int nonlinearity(int[] walshSpectrum) {
        int maxAbs = Arrays.stream(walshSpectrum)
                .map(Math::abs)
                .max()
                .orElse(0);
        return (walshSpectrum.length / 2) - (maxAbs / 2);
    }

    /**
     * Вычисляет корреляцию между двумя булевыми функциями
     * @param f первая булева функция
     * @param g вторая булева функция
     * @throws RuntimeException если количество переменных в функциях различно
     * @return корреляция между функциями
     * */
    public static double correlation(BooleanFunction f, BooleanFunction g) {
        if(f.getVariablesCount() != g.getVariablesCount()) {
            throw new RuntimeException("Разное количество аргументов!");
        }

        int size = f.getVariablesCount(), sum = 0;
        for (int x = 0; x < size; x++) {
            sum += (f.getBit(x) == g.getBit(x)) ? 1 : -1;
        }
        return (double) sum / size;
    }

    /**
     * Вычисляет спектр автокорреляции булевой функции
     * @param function булева функция
     * @return массив с коэффициентами автокорреляции булевой функции
     * */
    public static int[] autocorrelationSpectrum(BooleanFunction function) {
        int size = 1 << function.getVariablesCount(), total = 0;
        var spectrum = new int[size];
        for (int a = 0; a < size; a++) {
            total = 0;
            for (int x = 0; x < size; x++) {
                total += (function.getBit(x) == function.getBit(x ^ a)) ? 1 : -1;
            }
            spectrum[a] = total;
        }
        return spectrum;
    }

    /**
     * Вычисляет линейные характеристики булевой функции
     * @param function булева функция
     * @return матрица с линейными характеристиками (LAT таблица)
     * */
    public static int[][] latTable(BooleanFunction function) {
        int size = 1 << function.getVariablesCount(), total = 0;
        var lat = new int[size][size];

        boolean ax, bx, fx;
        for (int a = 0; a < size; a++) {
            for (int b = 0; b < size; b++) {
                total = 0;
                for (int x = 0; x < size; x++) {
                    ax = (Integer.bitCount(a & x) % 2 != 0);
                    fx = function.getBit(x);
                    bx = (Integer.bitCount(b & x) % 2 != 0);
                    total += (ax ^ fx ^ bx) ? -1 : 1;
                }
                lat[a][b] = total;
            }
        }
        return lat;
    }
}
