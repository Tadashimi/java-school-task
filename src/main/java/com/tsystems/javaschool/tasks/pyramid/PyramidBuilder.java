package com.tsystems.javaschool.tasks.pyramid;

import java.util.Iterator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if (inputNumbers == null) {
            throw new CannotBuildPyramidException("Input array must not be null");
        }

        try {
            inputNumbers.sort(((o1, o2) -> o2 - o1));
        } catch (Throwable e) {
            throw new CannotBuildPyramidException("Error while array sorting");
        }

        int inputArraySize = inputNumbers.size();

        int pyramidHeight = calculatePyramidHeight(inputArraySize);

        if (pyramidHeight == -1) {
            throw new CannotBuildPyramidException("Inappropriate number of elements in array");
        }

        return createPyramid(inputNumbers, pyramidHeight);
    }

    /**
     * Calculates pyramid length.
     * Number of elements in pyramid is a sum of first n terms in arithmetic progression with a(0) = 1 and d = 1.
     * So using formula S(n) = (2 * a(0) + d * (n - 1)) * n / 2 can find n - the pyramid height: n^2 + n - 2 * S(n) = 0.
     *
     * @param inputArraySize number of elements in input array
     * @return pyramid height or -1 if number of elements is incorrect
     */
    private int calculatePyramidHeight(int inputArraySize) {
        int discriminant = 1 + 4 * 2 * inputArraySize;

        if (discriminant < 0) {
            return -1;
        }

        Double calculatedPyramidHeight = (-1 + Math.sqrt(discriminant)) / 2;
        return (isInteger(calculatedPyramidHeight)) ? calculatedPyramidHeight.intValue() : -1;
    }

    /**
     * Checks if double value does not contain any decimal part.
     *
     * @param variable double variable for check
     * @return true if double does not contain any decimal part
     */
    private boolean isInteger(Double variable) {
        return (variable == Math.floor(variable)) && !Double.isInfinite(variable);
    }

    /**
     * Build array with pyramid.
     *
     * @param inputNumbers  - initial array after sorting in reverse order
     * @param pyramidHeight - calculated pyramid height
     * @return array with pyramid
     */
    private int[][] createPyramid(List<Integer> inputNumbers, int pyramidHeight) {
        int pyramidWeight = pyramidHeight * 2 - 1;
        int[][] result = new int[pyramidHeight][pyramidWeight];
        int countOfZeroesFromTheEndOfRow = 0;
        Iterator<Integer> arrayIterator = inputNumbers.iterator();

        for (int i = pyramidHeight - 1; i >= 0; i--) {
            int j = pyramidWeight - countOfZeroesFromTheEndOfRow - 1;

            while (j >= countOfZeroesFromTheEndOfRow) {
                result[i][j] = arrayIterator.next();
                j -= 2;
            }

            countOfZeroesFromTheEndOfRow++;
        }

        return result;
    }
}
