package com.lohika.tests.constraints.check;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel on 11/25/15.
 */
public class ConstraintsNotNullCheck {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/PrimaryKeys/";
    private static Map<DataType, Integer[]> types = new HashMap<DataType, Integer[]>();

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

    private enum ConstraintTypes {
        NAMELESS_ONE,
        NAMED_ONE;
    }

    private static int tableWidths[] = {10};
    private static int tableLengths[] = {10};
    private static int primaryKeyQuantity[] = {1, 4, 10};

    private static String getTableName(String dataType) {
        return dataType + "_WIDTH_LENGTH";
    }

    private static String getTableIndex(int width, int length) {
        return width + "_" + length;
    }

    private static String getTableIndex(int width, int length, int colSize) {
        return width + "_" + length + "_WITH_COLUMN_SIZE_" + ((colSize >= 0) ? colSize : "MINUS_" + Math.abs(colSize));
    }


    public static void createTests() {
        for (Map.Entry<DataType, Integer[]> type : types.entrySet()) {
            String resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            for (int pkQuantity : primaryKeyQuantity) {
                for (int wi : tableWidths) {
                    for (int le : tableLengths) {
                        for (int colSize : type.getValue()) {
                            for (ConstraintTypes currentConstraintType : ConstraintTypes.values()) {
                                String columns, tableIndex, tableName = getTableName(type.getKey().toString());
                                tableIndex = getTableIndex(wi, le, colSize);
                                switch (currentConstraintType) {
                                    case NAMELESS_ONE:
                                        changes.put("[CONSTRAINTS]", CommonPartsGeneration.generatePrimaryKeys(pkQuantity, "UNIQUE"));
                                        break;
                                    case NAMED_ONE:
                                        changes.put("[CONSTRAINTS]", CommonPartsGeneration.generatePrimaryKeys(pkQuantity, "UNIQUE", "CONST_" + tableName + "_" + tableIndex));
                                }
                                if (colSize < 0)
                                    columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, -101);
                                else
                                    columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, colSize);
                                changes.put("[TABLE_NAME]", tableName);
                                changes.put("[INDEX]", tableIndex);
                                changes.put("[DATA_TYPE]", type.getKey().toString());
                                changes.put("[PRIMARY_KEY_TYPE]", currentConstraintType.toString().replace("_", " "));
                                changes.put("[COLUMNS]", columns);
                                changes.put("[COLUMNS_IN_PRIMARY_KEY]", String.valueOf(pkQuantity));
                                changes.putAll(getStatements(type.getKey(), tableName + "_" + tableIndex, wi));
                                String test = PatternChanger.changePattern(CONSTRAINTS_PRIMARY_KEY_MULTIPLE_COLUMNS_TEST_PATTERN, changes);
                                resultString = resultString + test + "\n";
                            }
                        }
                    }
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + "MultipleColumns/" + type.getKey().toString() + "_unique_constraint_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, String> getStatements(DataType type, String tableName, int colCount) {
        List<String> columnNames = new ArrayList<String>();
        List<String> firstInsertValues;
        List<String> secondInsertValues;
        String neutralValue;

        for (int i = 1; i <= colCount; i++)
            columnNames.add("column_" + i);

        String insertPattern = "INSERT INTO %s (%s)\n VALUES (%s);";
        String updatePattern = "UPDATE %s SET COLUMN_1=%s WHERE COLUMN_1 = %s;";
        String deletePattern = "DELETE FROM %s WHERE COLUMN_1 = %s;";
        Map<String, String> result = new HashMap<String, String>();

        switch (type) {
            case SMALLINT:
                neutralValue = "8";
                firstInsertValues = createFirstValues("42", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case INTEGER:
                neutralValue = "80";
                firstInsertValues = createFirstValues("420", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case BIGINT:
                neutralValue = "800";
                firstInsertValues = createFirstValues("4200", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case REAL:
                neutralValue = "8.8";
                firstInsertValues = createFirstValues("42.42", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case DOUBLE:
                neutralValue = "80.8";
                firstInsertValues = createFirstValues("420.42", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case NUMERIC:
                neutralValue = "123";
                firstInsertValues = createFirstValues("1234", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case DECIMAL:
                neutralValue = "1234";
                firstInsertValues = createFirstValues("12345", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case FLOAT:
                neutralValue = "48346.0";
                firstInsertValues = createFirstValues("969180.574", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case DATE:
                neutralValue = "'2402-08-30'";
                firstInsertValues = createFirstValues("'2015-11-27'", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case TIME:
                neutralValue = "'20:37:55'";
                firstInsertValues = createFirstValues("'13:03:55'", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case TIMESTAMP:
                neutralValue = "'2010-01-09 08:43:13.717'";
                firstInsertValues = createFirstValues("'2015-01-06 23:39:58.211'", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case CHAR:
            case VARCHAR:
                neutralValue = "'C'";
                firstInsertValues = createFirstValues("'T'", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            case VARCHAR_FOR_BIT_DATA:
                neutralValue = "X'F5'";
                firstInsertValues = createFirstValues("X'E8'", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            default:
                throw new AssertionError("Unable to work with data type " + type);
        }

        assert firstInsertValues != null;
        result.put("[FIRST_INSERT]", String.format(insertPattern, tableName, listToStr(columnNames), listToStr(firstInsertValues)));
        result.put("[SECOND_INSERT]", String.format(insertPattern, tableName, listToStr(columnNames), listToStr(secondInsertValues)));
        result.put("[INVALID_UPDATE]", String.format(updatePattern, tableName, firstInsertValues.get(0), neutralValue));
        result.put("[DELETE]", String.format(deletePattern, tableName, neutralValue));
        result.put("[VALID_UPDATE]", String.format(updatePattern, tableName, neutralValue, firstInsertValues.get(0)));
        return result;
    }

    private static String listToStr(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String listItem : list)
            result.append(listItem).append(", ");
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        return result.toString();
    }

    private static List<String> createFirstValues(String template, int count) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < count; i++)
            result.add(template);
        return result;
    }

    private static List<String> createSecondValues(String neutralValue, List<String> firstValues) {
        List<String> secondInsertValues = new ArrayList<String>(firstValues);
        secondInsertValues.set(0, neutralValue);
        return secondInsertValues;
    }

    public static String CONSTRAINTS_PRIMARY_KEY_MULTIPLE_COLUMNS_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- (Constraints - Unique)\n" +
                    "-- (Tests table structure:\n" +
                    "-- (Data type: [DATA_TYPE])\n" +
                    "-- (Primary key type: [PRIMARY_KEY_TYPE])\n" +
                    "-- (Columns in primary key: [COLUMNS_IN_PRIMARY_KEY])\n" +
                    "-- Test will try to add correct and violating values to primary key created on several columns\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "DROP TABLE IF EXISTS [TABLE_NAME]_[INDEX];\n" +
                    "CREATE TABLE [TABLE_NAME]_[INDEX]([COLUMNS],\n[CONSTRAINTS]);\n" +
                    "\n" +
                    "-- Inserts\n" +
                    "[FIRST_INSERT]\n" +
                    "-- All OK\n" +
                    "\n" +
                    "[FIRST_INSERT]\n" +
                    "-- ERROR 23505: The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by '[CONSTRAINT_NAME]' defined on '[TABLE_NAME]_[INDEX]'.\n" +
                    "\n" +
                    "[SECOND_INSERT]\n" +
                    "-- All OK\n" +
                    "\n" +
                    "-- Updates\n" +
                    "[INVALID_UPDATE]\n" +
                    "-- ERROR 23505: The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by '[CONSTRAINT_NAME]' defined on '[TABLE_NAME]_[INDEX]'.\n" +
                    "\n" +
                    "[DELETE]\n" +
                    "\n" +
                    "[VALID_UPDATE]\n" +
                    "-- All OK\n" +
                    "SELECT * FROM [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- Drop check\n" +
                    "\n" +
                    "DROP TABLE [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- check if dropped\n" +
                    "SELECT * FROM [TABLE_NAME]_[INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[INDEX]' does not exist.\n\n";
}


