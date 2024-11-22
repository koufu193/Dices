package com.example.kf.utils;

import com.example.kf.Configuration;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public final class DiceUtils {
    public static int calcCombination(int sum, int sides, int dices) {
        if (dices == 0) return sum == 0 ? 1 : 0;

        return IntStream.range(0, sides)
                .map(i -> calcCombination(sum - (1 << i), sides, dices - 1))
                .sum();
    }

    public static boolean unableToExpress(int sum, int sides, int dices) {
        if (sum < dices || dices * maxRoll(sides) < sum) return true;
        return (sum >> (sides - 1)) + Integer.bitCount(sum & (maxRoll(sides) - 1)) > dices;
    }

    public static int maxRoll(int sides) {
        return 1 << (sides - 1);
    }

    public static int sumOf(int... diceExponents) {
        return Arrays.stream(diceExponents)
                .map(exponent -> (1 << exponent))
                .sum();
    }

    public static void forEachDiceCombination(int sum, int dices, Consumer<int[]> action) {
        _forEachDiceCombination(sum, Configuration.sides, 0, action, new int[dices]);
    }

    private static void _forEachDiceCombination(int sum, int sides, int index, Consumer<int[]> action, int[] exponents) {
        int next = index + 1;
        for (int rollExpo = 0; rollExpo < sides; rollExpo++) {
            exponents[index] = rollExpo;
            if (next < exponents.length) {
                _forEachDiceCombination(sum, sides, next, action, exponents);
            } else if (sumOf(exponents) == sum) {
                action.accept(exponents);
            }
        }
    }
}
