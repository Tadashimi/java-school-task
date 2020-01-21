package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("Lists must not be null");
        }

        int indexInShortSequence = 0;
        int shortSequenceSize = x.size();
        int indexInLongSequence = 0;
        int longSequenceSize = y.size();

        while (indexInShortSequence < shortSequenceSize &&
                indexInLongSequence < longSequenceSize) {
            if (x.get(indexInShortSequence) == y.get(indexInLongSequence)) {
                indexInShortSequence++;
            }
            indexInLongSequence++;
        }

        return (indexInShortSequence == shortSequenceSize);
    }
}
