package com.lohika.tests.comparisons;

/**
 * @author Andrey Zhelezny
 *         Date: 12/7/15
 */
public enum StringComparisonOperators {
    GREATER(" %s > %s "),
    LESSER(" %s < %s "),
    EQUALS(" %s = %s "),
    GREATER_OR_EQUALS(" %s >= %s "),
    LESSER_OR_EQUALS(" %s <= %s "),
    NOT_EQUALS(" %s <> %s "),
    LIKE(" %s LIKE '%s'"),
    BETWEEN(" %s BETWEEN %s AND %s "),
    NOT_BETWEEN(" %s NOT BETWEEN %s AND %s "),
    IN(" %s IN ( %s, %s )"),
    NOT_IN(" %s NOT IN ( %s, %s )");

    public static StringComparisonOperators[] getTwoArgsOperators() {
        return new StringComparisonOperators[]{GREATER, LESSER, EQUALS, GREATER_OR_EQUALS, LESSER_OR_EQUALS, NOT_EQUALS};
    }

    public static StringComparisonOperators[] getSpecialOperators() {
        return new StringComparisonOperators[]{LIKE};
    }

    private final String operator;

    StringComparisonOperators(String operator) {
        this.operator = operator;
    }

    public String getFormat() {
        return operator;
    }

    public String getFormat(String operand1, String operand2) {
        return String.format(operator, operand1, operand2);
    }

    public String getFormat(String operand1, String operand2, String operand3) {
        return String.format(operator, operand1, operand2, operand3);
    }
}
