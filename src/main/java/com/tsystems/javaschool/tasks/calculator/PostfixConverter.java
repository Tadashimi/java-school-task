package com.tsystems.javaschool.tasks.calculator;

import java.util.*;


public class PostfixConverter {
    private static final Character DIGIT_MARKER = '_';

    /*
     * Other operators, decimal delimiters and brackets can be added as correct symbols.
     * If add new operator it will be necessary to add operation to {@link Calculator} method calculateSingleOperations.
     */
    private static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList('-', '+', '/', '*'));
    private static final Set<Character> DECIMAL_DELIMITERS = new HashSet<>(Arrays.asList('.'));
    private static final Set<Character> OPENING_BRACKETS = new HashSet<>(Arrays.asList('('));
    private static final Set<Character> CLOSING_BRACKETS = new HashSet<>(Arrays.asList(')'));

    private static final Stack<Character> OPERATORS_STACK = new Stack<>();

    /**
     * Converts statement to postfix notation.
     * According Shunting-yard algorithm statement must be converted to postfix form.
     *
     * @param statement initial statement.
     * @return string contains initial statement in postfix form.
     * @throws IllegalArgumentException if initial statement is incorrect.
     */
    public static String convertToPostfixNotation(String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Input statement must not be null");
        }

        String statementWithoutWhiteSpaceSymbols = removeWhitespaceSymbolsStatement(statement);

        if (!isCorrectStatement(statementWithoutWhiteSpaceSymbols)) {
            throw new IllegalArgumentException("Input statement contains invalid symbols");
        }

        String workingStatement = highlightDigits(statementWithoutWhiteSpaceSymbols);

        StringBuilder resultStringBuilder = new StringBuilder();
        for (int i = 0; i < workingStatement.length(); i++) {
            Character currentChar = workingStatement.charAt(i);
            if (currentChar == DIGIT_MARKER) {
                resultStringBuilder.append(currentChar);
            }
            if (isNumber(currentChar)) {
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
                    throw new IllegalArgumentException("The expression might be malformed");
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
     * Checks if character is a number (can be decimal number).
     *
     * @param character to be checked.
     * @return {@code true} if character is number.
     */
    private static boolean isNumber(Character character) {
        return Character.isDigit(character) || DECIMAL_DELIMITERS.contains(character);
    }

    /**
     * Validates statement before convert to postfix form.
     *
     * @param statement to be checked.
     * @return {@code true} if statement does not contain invalid symbols.
     */
    private static boolean isCorrectStatement(String statement) {
        for (int i = 0; i < statement.length(); i++) {
            Character currentCharacter = statement.charAt(i);
            if (!Character.isDigit(currentCharacter) &&
                    !OPERATORS.contains(currentCharacter) &&
                    !DECIMAL_DELIMITERS.contains(currentCharacter) &&
                    !OPENING_BRACKETS.contains(currentCharacter) &&
                    !CLOSING_BRACKETS.contains(currentCharacter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes all whitespace character and adds symbol before all digits.
     *
     * @param statement input arithmetic expression.
     * @return string without whitespace character.
     */
    private static String removeWhitespaceSymbolsStatement(String statement) {
        return statement.replaceAll("\\s+", "");
    }

    /**
     * Adds marker before each number.
     *
     * @param statement input arithmetic expression.
     * @return string marked digits.
     */
    private static String highlightDigits(String statement) {
        return statement.replaceAll(formRegexForNumbers(), DIGIT_MARKER + "$1");
    }

    /**
     * Creates a regex string with delimiters to indicate numbers in statement.
     *
     * @return regex string for digit.
     */
    private static String formRegexForNumbers() {
        StringJoiner delimitersPart = new StringJoiner("|");
        for (Character character : DECIMAL_DELIMITERS) {
            delimitersPart.add("\\" + character);
        }
        String delimiters = delimitersPart.toString();

        return "(\\d+(" + delimiters + "\\d+)?)";
    }

    /**
     * Check if character is opening bracket
     *
     * @param character to be checked
     * @return {@code true} if character is opening bracket symbol
     */
    private static boolean isOpeningBracket(Character character) {
        return OPENING_BRACKETS.contains(character);
    }

    /**
     * Check if character is closing bracket
     *
     * @param character to be checked
     * @return {@code true} if character is closing bracket symbol
     */
    private static boolean isClosingBracket(Character character) {
        return CLOSING_BRACKETS.contains(character);
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
