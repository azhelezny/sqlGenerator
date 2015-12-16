package com.lohika.tests.window.functions;

import com.lohika.CommonPartsGeneration;
import com.lohika.types.DataType;

/**
 * @author Andrey Zhelezny
 *         Date: 12/15/15
 */
public class WFUtils {
    public static int rowCount = 30;
    private static final long maxOffsetValue = 2147483647;

    public enum NullsPolicy {
        Ignore("IGNORE NULLS"),
        Respect("RESPECT NULLS"),
        Absent("");

        private final String value;

        NullsPolicy(String s) {
            this.value = s;
        }

        public String get() {
            return this.value;
        }
    }

    public enum Offset {
        Zero("0", false),
        MinusOne("-1", false),
        Three("3", true),
        RowCount(String.valueOf(rowCount), true),
        RowCountMinus(String.valueOf(rowCount - 1), true),
        RowCountPlus(String.valueOf(rowCount + 1), true),
        Null("NULL", false),
        Max(String.valueOf(maxOffsetValue - 1), true),
        MaxPlusOne(String.valueOf(maxOffsetValue + 2), false),
        MaxMinusOne(String.valueOf(maxOffsetValue - 2), true),
        Absent("", true),
        Incorrect("'a'", false);

        Offset(String s, boolean isPositive) {
            this.value = s;
            this.isPositive = isPositive;
        }

        public String get() {
            return value;
        }

        public boolean isPositive() {
            return isPositive;
        }

        private String value;
        private boolean isPositive;
    }

    public enum OrderBy {
        Asc("ORDER BY column_1 ASC"),
        AscNullFirst("ORDER BY column_1 ASC NULLS FIRST"),
        AscNullLast("ORDER BY column_1 ASC NULLS LAST"),
        Desc("ORDER BY column_1 DESC"),
        DescNullFirst("ORDER BY column_1 DESC NULLS FIRST"),
        DescNullLast("ORDER BY column_1 DESC NULLS LAST"),
        Default("");

        private String value;

        OrderBy(String s) {
            this.value = s;
        }

        public String get() {
            return this.value;
        }
    }

    public enum PartitionByType {
        COLUMN1("PARTITION BY column_1"),
        COLUMN2("PARTITION BY column_2");

        private final String value;

        PartitionByType(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }
    }

    public enum MaxMinOptions {
        Distinct("DISTINCT"),
        All("ALL"),
        Absent("");


        private final String value;

        MaxMinOptions(String s) {
            this.value = s;
        }

        public String get() {
            return this.value;
        }
    }


    public enum DefaultValueType {
        Absent,
        Null,
        Matched,
        NotMatched
    }

    /*public static String getDefaultValue(DataType type, DefaultValueType valueType) {
        if (valueType.equals(DefaultValueType.Absent)) return "";
        if (valueType.equals(DefaultValueType.Null)) return "NULL";

        switch (type) {
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case REAL:
            case DOUBLE:
            case FLOAT:
            case DECIMAL:
            case NUMERIC:
                if (valueType.equals(DefaultValueType.Matched))
                    return "42";
                else
                    return "'not valid'";
            case DATE:
            case TIME:
            case TIMESTAMP:
            case CHAR:
            case VARCHAR:
            case LONG_VARCHAR:
            case CLOB:
                if (valueType.equals(DefaultValueType.Matched))
                    return "'Str'";
                else
                    return "42";
            case CHAR_FOR_BIT_DATA:
            case VARCHAR_FOR_BIT_DATA:
            case LONG_VARCHAR_FOR_BIT_DATA:
            case BLOB:
                if (valueType.equals(DefaultValueType.Matched))
                    return "X'F5'";
                else
                    return "42";
            case BOOLEAN:
                if (valueType.equals(DefaultValueType.Matched))
                    return "true";
                else
                    return "42";
            default:
                throw new NullPointerException("Invalid type " + type.toString());
        }
    }*/

    public static String getTable(DataType[] types, String tableName, int colCommonLength) {
        return "CREATE TABLE " + tableName + " (" + CommonPartsGeneration.generateColumns(types, "column_", colCommonLength) + ");";
    }

    public static String getInserts(String tableName, DataType[] types, int insertsCount) {
        assert insertsCount >= 1;
        String result = CommonPartsGeneration.getInsert(tableName, types) + "\n";
        for (int i = 1; i < insertsCount; i++) {
            result += CommonPartsGeneration.getInsert(tableName, types) + "\n";
        }
        return result;
    }
}
