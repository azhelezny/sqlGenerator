package com.lohika;

import com.lohika.types.DataType;

import java.util.Set;

/**
 * @author Pavel on 11/24/15.
 */
public class CommonPartsGeneration {

    public static String generateColumns(DataType[] types, String columnPrefix, int columnsLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            String typeString = String.format(types[i].toFormatString(), (columnsLength < -2) ? "" : "(" + columnsLength + ")");
            result.append(columnPrefix).append(i+1).append(" ").append(typeString).append(", ");
        }
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

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

    public static String generateColumnsSameTypeWithPrdefinedNotNullConstraint(DataType type, int columnsNumber, int columnsLength, Set<Integer> notNullsColumns) {
        StringBuilder result = new StringBuilder();
        String typeString = String.format(type.toFormatString(), (columnsLength < -100) ? "" : "(" + columnsLength + ")");
        for (Integer i = 1; i <= columnsNumber; i++) {
            result.append("column_").append(i).append(" ").append(typeString);
            if (notNullsColumns.contains(i))
                result.append(" NOT NULL");
            result.append(", ");
        }
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static String generateNotNullColumnsSameType(DataType type, int columnsNumber, int columnsLength) {
        StringBuilder result = new StringBuilder();
        String typeString = String.format(type.toFormatString(), (columnsLength < -100) ? "" : "(" + columnsLength + ")");
        for (int i = 1; i <= columnsNumber; i++)
            result.append("column_").append(i).append(" ").append(typeString).append(" NOT NULL, ");
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
