package com.lohika.tests.constraints.check;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.*;

/**
 * @author Pavel on 11/25/15.
 */
public class NotNullTests {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Not_Null/";
    private static Map<DataType, Integer[]> types = new HashMap<DataType, Integer[]>();

    private static final String insertPattern = "INSERT INTO %s (%s)\n VALUES (%s);";
    private static final String deletePattern = "DELETE FROM %s WHERE COLUMN_1 IS NULL;";
    private static final String alterAddPattern = "ALTER TABLE %s ALTER COLUMN %s NOT NULL;";
    private static final String alterDeletePattern = "ALTER TABLE %s ALTER COLUMN %s NULL;";


    static {
        types.put(DataType.SMALLINT, new Integer[]{-1});
        types.put(DataType.INTEGER, new Integer[]{-1});
        types.put(DataType.BIGINT, new Integer[]{-1});
        types.put(DataType.REAL, new Integer[]{-1});
        types.put(DataType.DOUBLE, new Integer[]{-1});
        types.put(DataType.NUMERIC, new Integer[]{-1, 10});
        types.put(DataType.DECIMAL, new Integer[]{-1, 10});
        types.put(DataType.FLOAT, new Integer[]{-1, 23});
        types.put(DataType.DATE, new Integer[]{-1});
        types.put(DataType.TIME, new Integer[]{-1});
        types.put(DataType.TIMESTAMP, new Integer[]{-1});
        types.put(DataType.CHAR, new Integer[]{-1, 10});
        types.put(DataType.VARCHAR, new Integer[]{1, 10});
        types.put(DataType.VARCHAR_FOR_BIT_DATA, new Integer[]{1, 10});
    }

    public static String getInsertTwoColumns(String tableName, String val1, String val2) {
        return String.format(insertPattern, tableName, "column_1, column_2", val1 + ", " + val2);
    }

    public static String getInsert(String tableName, String val1) {
        return String.format(insertPattern, tableName, "column_1", val1);
    }

    private static String getValueForType(DataType type) {
        switch (type) {
            case SMALLINT:
                return "42";
            case INTEGER:
                return "420";
            case BIGINT:
                return "4200";
            case REAL:
                return "42.42";
            case DOUBLE:
                return "420.42";
            case NUMERIC:
                return "1234";
            case DECIMAL:
                return "12345";
            case FLOAT:
                return "969180.574";
            case DATE:
                return "'2015-11-27'";
            case TIME:
                return "'13:03:55'";
            case TIMESTAMP:
                return "'2015-01-06 23:39:58.211'";
            case CHAR:
            case VARCHAR:
                return "'T'";
            case VARCHAR_FOR_BIT_DATA:
                return "X'E8'";
            default:
                throw new AssertionError("Unable to work with data type " + type);
        }
    }

    private static String listToStr(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String listItem : list)
            result.append(listItem).append(", ");
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        return result.toString();
    }

    private static String getTableName(String dataType) {
        return dataType + "_WIDTH_LENGTH";
    }

    private static String getTableIndex(int width, int length, int colSize) {
        return width + "_" + length + "_WITH_COLUMN_SIZE_" + ((colSize >= 0) ? colSize : "MINUS_" + Math.abs(colSize));
    }

    private static String getColumns(int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= count; i++)
            result.append("column_").append(i).append(", ");
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static void createTests() {
        for (Map.Entry<DataType, Integer[]> type : types.entrySet()) {
            String resultString = "";
            Set<Integer> notNullColumns = new HashSet<Integer>();
            notNullColumns.add(2);
            Map<String, String> changes = new HashMap<String, String>();
            for (int colSize : type.getValue()) {
                String columns, column, tableIndex, tableName = getTableName(type.getKey().toString());
                tableIndex = getTableIndex(2, 2, colSize);
                if (colSize < 0) {
                    columns = CommonPartsGeneration.generateColumnsSameTypeWithPrdefinedNotNullConstraint(type.getKey(), 2, -101, notNullColumns);
                    column = CommonPartsGeneration.generateColumnsSameType(type.getKey(), 1, -101);
                } else {
                    columns = CommonPartsGeneration.generateColumnsSameTypeWithPrdefinedNotNullConstraint(type.getKey(), 2, colSize, notNullColumns);
                    column = CommonPartsGeneration.generateColumnsSameType(type.getKey(), 1, colSize);
                }
                String tableFullName = tableName + "_" + tableIndex;
                changes.put("[TABLE_NAME]", tableName);
                changes.put("[INDEX]", tableIndex);
                changes.put("[DATA_TYPE]", type.getKey().toString());
                changes.put("[COLUMNS]", columns);
                changes.put("[COLUMN]", column);

                String value = getValueForType(type.getKey());
                changes.put("[INSERT_NULL_NULL]", getInsertTwoColumns(tableFullName, "NULL", "NULL"));
                changes.put("[INSERT_VAL_NULL]", getInsertTwoColumns(tableFullName, value, "NULL"));
                changes.put("[INSERT_VAL]", getInsert(tableFullName, value));
                changes.put("[INSERT_NULL]", getInsert(tableFullName, "NULL"));
                changes.put("[INSERT_NULL_VAL]", getInsertTwoColumns(tableFullName, "NULL", value));
                changes.put("[INSERT_VAL_VAL]", getInsertTwoColumns(tableFullName, value, value));

                changes.put("[ALTER_STATEMENT_ADD_NOT_NULL]", String.format(alterAddPattern, tableFullName, "column_1"));
                changes.put("[ALTER_STATEMENT_REMOVE_NOT_NULL]", String.format(alterDeletePattern, tableFullName, "column_1"));
                changes.put("[DELETE_NULLS]", String.format(deletePattern, tableFullName));

                String test = PatternChanger.changePattern(NOT_NULL_TEST_PATTERN, changes);
                resultString = resultString + test + "\n";
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + type.getKey().toString() + "_not_null_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static String NOT_NULL_TEST_PATTERN =
            "-- *************************************** ***********************************\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "-- (Constraints - NOT_NULL)\n" +
                    "-- (Data type: [DATA_TYPE])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "-- TEST 1:  CREATION OF TABLE WITH 'NOT NULL' COLUMN AND CHECKING OF (IM)POSSIBILITY TO INSERT INTO IT NULLS OR VALUES\n" +
                    "-- splicetest: ignore-output start\n" +
                    "drop table if exists [TABLE_NAME]_[INDEX];\n" +
                    "-- splicetest: ignore-output stop\n" +
                    "CREATE TABLE [TABLE_NAME]_[INDEX]([COLUMNS]);\n" +
                    "[INSERT_NULL_NULL]\n" +
                    "-- ERROR 23502: Column 'COLUMN_2'  cannot accept a NULL value.\n" +
                    "[INSERT_VAL_NULL]\n" +
                    "-- ERROR 23502: Column 'COLUMN_2'  cannot accept a NULL value.\n" +
                    "[INSERT_VAL]\n" +
                    "-- ERROR 23502: Column 'COLUMN_2'  cannot accept a NULL value.\n" +
                    "[INSERT_NULL_VAL]\n" +
                    "-- All OK\n" +
                    "[INSERT_VAL_VAL]\n" +
                    "-- All OK\n" +
                    "DROP TABLE [TABLE_NAME]_[INDEX];\n" +
                    "-- check if dropped\n" +
                    "SELECT * FROM [TABLE_NAME]_[INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[INDEX]' does not exist.\n" +
                    "\n" +
                    "-- TEST 2: CHECK ABILITY TO USE 'ALTER' STATEMENT TO ADD/REMOVE 'NOT NULL' CONSTRAINT\n" +
                    "CREATE TABLE [TABLE_NAME]_[INDEX]([COLUMN]);\n" +
                    "[INSERT_VAL]\n" +
                    "[INSERT_NULL]\n" +
                    "[ALTER_STATEMENT_ADD_NOT_NULL]\n" +
                    "-- ERROR X0Y80: ALTER table '\"SPLICE\".\"[TABLE_NAME]_[INDEX]\"' failed. Null data found in column 'COLUMN_1'.\n" +
                    "[INSERT_NULL]\n" +
                    "-- All OK\n" +
                    "[DELETE_NULLS]\n" +
                    "[ALTER_STATEMENT_ADD_NOT_NULL]\n" +
                    "-- All OK\n" +
                    "[INSERT_VAL]\n" +
                    "-- All OK\n" +
                    "[INSERT_NULL]\n" +
                    "-- ERROR 23502: Column 'COLUMN_1'  cannot accept a NULL value.\n" +
                    "[ALTER_STATEMENT_REMOVE_NOT_NULL]\n" +
                    "[INSERT_NULL]\n" +
                    "-- All OK\n" +
                    "[DELETE_NULLS]\n" +
                    "[ALTER_STATEMENT_ADD_NOT_NULL]\n" +
                    "-- All OK\n" +
                    "[INSERT_NULL]\n" +
                    "-- ERROR 23502: Column 'COLUMN_1'  cannot accept a NULL value.\n" +
                    "DROP TABLE [TABLE_NAME]_[INDEX];\n" +
                    "-- check if dropped\n" +
                    "SELECT * FROM [TABLE_NAME]_[INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[INDEX]' does not exist.\n" +
                    "\n";
}


