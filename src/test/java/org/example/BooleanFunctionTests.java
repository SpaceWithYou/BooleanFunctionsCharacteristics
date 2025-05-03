package org.example;

import org.example.functions.BooleanFunction;
import org.example.functions.algos.Algorithms;
import org.example.util.Converter;
import org.example.util.Parser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BooleanFunctionTests {

    @Test
    void testParserValidInput() throws Exception {
        var input = "1100";
        var reader = new BufferedReader(new StringReader(input));
        var parser = new Parser(reader);

        var function = parser.parse();

        assertEquals(2, function.getVariablesCount());
        assertTrue(function.getBits().get(0));
        assertTrue(function.getBits().get(1));
        assertFalse(function.getBits().get(2));
        assertFalse(function.getBits().get(3));
    }

    @Test
    void testParserInvalidInput() {
        var input = "1234";
        var reader = new BufferedReader(new StringReader(input));
        var parser = new Parser(reader);

        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    @Test
    void testValueVectorToDNF() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var dnf = Converter.valueVectorToDNF(function);

        assertEquals(2, dnf.size());
        assertTrue(dnf.contains(List.of(-1, -2)));
        assertTrue(dnf.contains(List.of(1, -2)));
    }

    @Test
    void testMoebiusTransform() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var transformed = Algorithms.moebiusTransform(function);

        assertNotNull(transformed);
        assertFalse(transformed.get(0));
        assertTrue(transformed.get(1));
        assertFalse(transformed.get(2));
        assertTrue(transformed.get(3));
    }

    @Test
    void testWalshTransform() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var walshSpectrum = Algorithms.walshTransform(function);

        assertEquals(4, walshSpectrum.length);
        assertArrayEquals(new int[]{0, 4, 0, 0}, walshSpectrum);
    }

    @Test
    void testAlgebraicDegree() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var anf = Converter.valueVectorToANF(function);
        var degree = Algorithms.algebraicDegree(anf);

        assertEquals(1, degree);
    }

    @Test
    void testIsAffine() {
        var bits = BitSet.valueOf(new long[]{0b1111});
        var function = new BooleanFunction(bits, 2);

        var anf = Converter.valueVectorToANF(function);
        var isAffine = Algorithms.isAffine(anf);

        assertTrue(isAffine);
    }

    @Test
    void testHammingWeight() {
        var bits = BitSet.valueOf(new long[]{0b1011});
        var function = new BooleanFunction(bits, 2);

        var weight = Algorithms.hammingWeight(function);

        assertEquals(3, weight);
    }

    @Test
    void testNonlinearity() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var walshSpectrum = Algorithms.walshTransform(function);
        var nonlinearity = Algorithms.nonlinearity(walshSpectrum);

        assertEquals(0, nonlinearity);
    }

    @Test
    void testCorrelation() {
        var bits1 = BitSet.valueOf(new long[]{0b1110});
        var bits2 = BitSet.valueOf(new long[]{0b1100});
        var function1 = new BooleanFunction(bits1, 2);
        var function2 = new BooleanFunction(bits2, 2);

        var correlation = Algorithms.correlation(function1, function2);

        assertEquals(0.5, correlation);
    }

    @Test
    void testAutocorrelationSpectrum() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var spectrum = Algorithms.autocorrelationSpectrum(function);

        assertArrayEquals(new int[]{4, -4, 4, -4}, spectrum);
    }

    @Test
    void testLatTable() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var lat = Algorithms.latTable(function);

        assertNotNull(lat);
        assertEquals(4, lat.length);
        assertEquals(4, lat[0].length);
    }

    @Test
    void testValueVectorToCNF() {
        var bits = BitSet.valueOf(new long[]{0b1010});
        var function = new BooleanFunction(bits, 2);

        var cnf = Converter.valueVectorToCNF(function);

        assertEquals(2, cnf.size());
        assertTrue(cnf.contains(List.of(-1, -2)));
        assertTrue(cnf.contains(List.of(1, -2)));
    }
}