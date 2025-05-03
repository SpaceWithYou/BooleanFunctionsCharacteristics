package org.example.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.BitSet;

/**
 * Класс, представляющий булеву функцию
 * */
@Getter
@AllArgsConstructor
public final class BooleanFunction {
    private BitSet bits;
    private int variablesCount;

    public boolean getBit(int index) {
        return bits.get(index);
    }
}
