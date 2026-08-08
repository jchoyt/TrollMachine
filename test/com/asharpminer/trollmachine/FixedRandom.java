package com.asharpminer.trollmachine;

import java.util.Random;

/**
 * A Random whose nextDouble() always returns the same value, so tests can
 * drive Math.random()-based logic deterministically.
 */
class FixedRandom extends Random {
    private final double value;

    FixedRandom(double value) {
        this.value = value;
    }

    @Override
    public double nextDouble() {
        return value;
    }
}
