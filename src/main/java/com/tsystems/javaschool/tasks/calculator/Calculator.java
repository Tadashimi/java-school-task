package com.tsystems.javaschool.tasks.calculator;

import java.util.EmptyStackException;
import java.util.Stack;

import static com.tsystems.javaschool.tasks.calculator.PostfixConverter.convertToPostfixNotation;
import static com.tsystems.javaschool.tasks.calculator.PostfixConverter.isOperator;

public class Calculator {

    /**
     * Evaluates statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        try {
            String postfixNotation = convertToPostfixNotation(statement);
            Double calculatedResult = calculateResult(postfixNotation);
            if (calculatedResult == null ||
                    calculatedResult.isInfinite() ||
                    calculatedResult.isNaN()) {
                return null;
            }
            if (isInteger(calculatedResult)) {
                return Integer.valueOf(calculatedResult.intValue()).toString();
            } else {
                return calculatedResult.toString();
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Calculates expression in postfix form
     *
     * @param postfixNotation statement in postfix notation
     * @return calculated value
     */
    private static Double calculateResult(String postfixNotation) {
        Stack<Double> operandStack = new Stack<>();

        boolean isUnderscore = false;
        StringBuilder numberString = new StringBuilder();

        for (int i = 0; i < postfixNotation.length(); i++) {
            if (postfixNotation.charAt(i) == '_') {
                isUnderscore = true;
                if (i > 0 && numberString.length() > 0) operandStack.push(Double.parseDouble(numberString.toString()));
                numberString.delete(0, numberString.length());
                continue;
            }
            boolean isOperator = isOperator(postfixNotation.charAt(i));
            if (isUnderscore) {
                if (!isOperator) {
                    numberString.append(postfixNotation.charAt(i));
                    continue;
                } else {
                    operandStack.push(Double.parseDouble(numberString.toString()));
                    numberString.delete(0, numberString.length());
                    isUnderscore = false;
                }
            }
            if (isOperator) {
                try {
                    Double secondDigit = operandStack.pop();
                    Double firstDigit = operandStack.pop();
                    double result = calculateSingleOperations(firstDigit, secondDigit, postfixNotation.charAt(i));
                    operandStack.push(result);
                } catch (EmptyStackException e) {
                    return null;
                }
            }
        }

        try {
            return operandStack.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    /**
     * Perform a single operations
     *
     * @param firstDigit  participate in operation
     * @param secondDigit participate in operation
     * @param operator    type of operation
     * @return result of operation
     * @throws IllegalArgumentException when character operator is incorrect
     */
    private static double calculateSingleOperations(Double firstDigit, Double secondDigit, Character operator) {
        switch (operator) {
            case '+':
                return firstDigit + secondDigit;
            case '-':
                return firstDigit - secondDigit;
            case '*':
                return firstDigit * secondDigit;
            case '/':
                return firstDigit / secondDigit;
            default:
                throw new IllegalArgumentException("Illegal operator");

        }
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

}
