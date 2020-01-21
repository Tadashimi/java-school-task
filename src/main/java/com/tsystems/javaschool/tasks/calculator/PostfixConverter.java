package com.tsystems.javaschool.tasks.calculator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class PostfixConverter {
    private static final Character DIGIT_MARKER = '_';
    private static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList('-', '+', '/', '*'));
    private static final Stack<Character> OPERATORS_STACK = new Stack<>();

    /**
     * Converts statement to postfix notation.
     * According Shunting-yard algorithm statement must be converted to postfix form.
     *
     * @param statement initial statement
     * @return string contains initial statement in postfix form
     * @throws IllegalArgumentException if initial statement is incorrect
     */
    public static String convertToPostfixNotation(String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Input statement must not be null");
        }

        String workingStatement = prepareStatement(statement);

        StringBuilder resultStringBuilder = new StringBuilder();
        for (int i = 0; i < workingStatement.length(); i++) {
            Character currentChar = workingStatement.charAt(i);
            if (currentChar == DIGIT_MARKER) {
                resultStringBuilder.append(currentChar);
            }
            if (Character.isLetterOrDigit(currentChar)) {
                resultStringBuilder.append(currentChar);
            } else if (isOpeningBracket(currentChar)) {
                if (i > 0 && (!isOperator(workingStatement.charAt(i - 1))
                        && !isOpeningBracket(workingStatement.charAt(i - 1)))) {
                    throw new IllegalArgumentException("There should be an operator before a opening bracket");
                }
                OPERATORS_STACK.push(currentChar);
            } else if (isOperator(currentChar)) {
                if (i == 0 || i == workingStatement.length() - 1) {
                    throw new IllegalArgumentException("The operator should not be at the start or end of the expression");
                }
                if (isOperator(workingStatement.charAt(i + 1))) {
                    throw new IllegalArgumentException("Two operators should not occur consecutively");
                }
                while (!OPERATORS_STACK.isEmpty()
                        && !isOpeningBracket(OPERATORS_STACK.peek())
                        && hasHigherPrecedence(OPERATORS_STACK.peek(), currentChar)) {
                    resultStringBuilder.append(OPERATORS_STACK.pop());
                }
                OPERATORS_STACK.push(currentChar);
            } else if (isClosingBracket(currentChar)) {
                if (i > 0 && isOperator(workingStatement.charAt(i - 1))) {
                    throw new IllegalArgumentException("There should not be an operator");
                }
                while (!OPERATORS_STACK.isEmpty() && !isOpeningBracket(OPERATORS_STACK.peek())) {
                    resultStringBuilder.append(OPERATORS_STACK.pop());
                }
                Character poppedChar = OPERATORS_STACK.pop();
                if (!isOpeningBracket(poppedChar)) {
                    throw new IllegalArgumentException("The expression might be malformed!");
                }
            }
        }

        while (!OPERATORS_STACK.isEmpty()) {
            Character pop = OPERATORS_STACK.pop();
            if (isOpeningBracket(pop)) {
                throw new IllegalArgumentException("The expression might contain extra opening brackets");
            }
            resultStringBuilder.append(pop);
        }
        return resultStringBuilder.toString();
    }

    /**
     * Removes all whitespace character and adds symbol before all digits.
     *
     * @param statement input arithmetic expression
     * @return string without whitespace character and with market digits
     */
    private static String prepareStatement(String statement) {
        String statementWithoutSpaces = statement.replaceAll("\\s+", "");
        return statementWithoutSpaces.replaceAll("(\\w+)", DIGIT_MARKER + "$1");
    }

    /**
     * Check if character is opening bracket
     *
     * @param character to be checked
     * @return {@code true} if character is opening bracket symbol
     */
    private static boolean isOpeningBracket(Character character) {
        return character == '(';
    }

    /**
     * Check if character is closing bracket
     *
     * @param character to be checked
     * @return {@code true} if character is closing bracket symbol
     */
    private static boolean isClosingBracket(Character character) {
        return character == ')';
    }

    /**
     * Check if character is operator symbol
     *
     * @param character to be checked
     * @return {@code true} if character is operator symbol
     */
    public static boolean isOperator(Character character) {
        return OPERATORS.contains(character);
    }

    /**
     * Checks if the first operator has higher priority compared to the second
     *
     * @param firstOperator  to compare the priority
     * @param secondOperator to compare the priority
     * @return {@code true} if the fist operator has higher precedence than the second
     * @throws IllegalArgumentException if any operator is null
     */
    private static boolean hasHigherPrecedence(Character firstOperator, Character secondOperator) {
        if (firstOperator == null || secondOperator == null) {
            throw new IllegalArgumentException("The operators must not be null");
        }

        return priorityOfOperator(firstOperator) - priorityOfOperator(secondOperator) >= 0;
    }

    /**
     * Gets the priority of operator.
     *
     * @param operator to find its priority
     * @return the priority of operation
     */
    private static int priorityOfOperator(Character operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}
