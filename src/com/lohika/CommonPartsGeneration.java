package com.lohika;

import com.lohika.inserts.*;
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
            result.append(columnPrefix).append(i + 1).append(" ").append(typeString).append(", ");
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

    public static String getInsert(String tableName, DataType[] types) {
        String insertPattern = "INSERT INTO %s (%s) values (%s);";

        StringBuilder columns = new StringBuilder();
        for (int i = types.length; i != 0; i--)
            columns.append("column_").append(i).append(", ");
        columns.deleteCharAt(columns.length() - 1);
        columns.deleteCharAt(columns.length() - 1);

        StringBuilder values = new StringBuilder();

        for (int i = types.length - 1; i >= 0; i--) {
            DataType type = types[i];
            assert type != null;
            switch (type) {
                case BOOLEAN:
                    values.append(new BooleanInsert(tableName).getRandomValue()).append(", ");
                    break;
                case CHAR_FOR_BIT_DATA:
                case VARCHAR_FOR_BIT_DATA:
                case LONG_VARCHAR_FOR_BIT_DATA:
                case BLOB:
                    values.append(new CharForBitDataInsert(tableName).getRandomValue()).append(", ");
                    break;
                case LONG_VARCHAR:
                case CHAR:
                case VARCHAR:
                case CLOB:
                    values.append(new CharInsert(tableName).getRandomValue()).append(", ");
                    break;
                case SMALLINT:
                    values.append(new SmallintInsert(tableName).getRandomValue()).append(", ");
                    break;
                case INTEGER:
                    values.append(new IntegerInsert(tableName).getRandomValue()).append(", ");
                    break;
                case BIGINT:
                    values.append(new BigintInsert(tableName).getRandomValue()).append(", ");
                    break;
                case FLOAT:
                    values.append(new FloatInsert(tableName).getRandomValue()).append(", ");
                    break;
                case REAL:
                    values.append(new RealInsert(tableName).getRandomValue()).append(", ");
                    break;
                case DECIMAL:
                case NUMERIC:
                    values.append(new DecimalInsert(tableName).getRandomValue()).append(", ");
                    break;
                case DOUBLE:
                    values.append(new DoubleInsert(tableName).getRandomValue()).append(", ");
                    break;
                case DATE:
                    values.append(new DateInsert(tableName).getRandomValue()).append(", ");
                    break;
                case TIME:
                    values.append(new TimeInsert(tableName).getRandomValue()).append(", ");
                    break;
                case TIMESTAMP:
                    values.append(new TimestampInsert(tableName).getRandomValue()).append(", ");
                    break;
                default:
                    throw new NullPointerException("Incorrect type:" + type.toString());
            }
        }
        values.deleteCharAt(values.length() - 1);
        values.deleteCharAt(values.length() - 1);

        return String.format(insertPattern, tableName, columns.toString(), values.toString());
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
