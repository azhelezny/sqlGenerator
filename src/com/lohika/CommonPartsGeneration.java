package com.lohika;

import com.lohika.types.DataType;

/**
 * @author Pavel on 11/24/15.
 */
public class CommonPartsGeneration {
    public static String generateColumnsSameType(String type, int columnsNumber) {
        String result = "";
        for (int i = 1; i <= columnsNumber; i++) {
            if (i == 1) {
                result = result + "column_" + i + " " + type;
            } else {
                result = result + ", column_" + i + " " + type;
            }
        }
        return result;
    }

    public static String generateColumnsSameType(DataType type, int columnsNumber, int columnsLength) {
        StringBuilder result = new StringBuilder();
        String typeString = String.format(type.toFormatString(), (columnsLength < -100) ? "" : "(" + columnsLength + ")");
        for (int i = 1; i <= columnsNumber; i++)
            result.append("column_").append(i).append(" ").append(typeString).append(", ");
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static String generatePrimaryKeys(int count, String type) {
        StringBuilder result = new StringBuilder();
        result.append(type.toUpperCase()).append(" (");
        for (int i = 1; i <= count; i++)
            result.append("column_").append(i).append(", ");
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        result.append(")");
        return result.toString();
    }

    public static String generatePrimaryKeys(int count, String type, String name) {
        StringBuilder result = new StringBuilder();
        result.append("CONSTRAINT ").append(name).append(type.toUpperCase()).append(" (");
        for (int i = 1; i <= count; i++)
            result.append("column_").append(i).append(", ");
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        result.append(")");
        return result.toString();
    }
}
