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
public class PrimaryKeysMultColumsConstraints {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/PrimaryKeys/";
    private static Map<DataType, Integer[]> types = new HashMap<DataType, Integer[]>();

    static {
        types.put(DataType.SMALLINT, new Integer[]{-1});
        /*types.put(DataType.INTEGER, new Integer[]{-1});
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
        types.put(DataType.CHAR_FOR_BIT_DATA, new Integer[]{-1, 10});
        types.put(DataType.VARCHAR, new Integer[]{1, 10});
        types.put(DataType.LONG_VARCHAR, new Integer[]{-1});
        types.put(DataType.VARCHAR_FOR_BIT_DATA, new Integer[]{1, 10});
        types.put(DataType.LONG_VARCHAR_FOR_BIT_DATA, new Integer[]{-1});*/
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
                for (ConstraintTypes currentConstraintType : ConstraintTypes.values()) {
                    switch (currentConstraintType) {
                        case NAMELESS_ONE:
                            changes.put("[CONSTRAINTS]", CommonPartsGeneration.generatePrimaryKeys(pkQuantity));
                            break;
                        case NAMED_ONE:
                            changes.put("[CONSTRAINTS]", CommonPartsGeneration.generatePrimaryKeys(pkQuantity, "CONST_" + type.getKey().toString()));
                    }
                    for (int wi : tableWidths) {
                        for (int le : tableLengths) {
                            for (int colSize : type.getValue()) {
                                String columns, tableIndex, tableName = getTableName(type.getKey().toString());
                                tableIndex = getTableIndex(wi, le, colSize);
                                columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, colSize);
                                changes.put("[TABLE_NAME]", tableName);
                                changes.put("[INDEX]", tableIndex);
                                changes.put("[DATA_TYPE]", type.getKey().toString());
                                changes.put("[COLUMNS]", columns);
                                changes.putAll(getStatements(type.getKey(), tableName + "_" + tableIndex, wi));
                                String test = PatternChanger.changePattern(CONSTRAINTS_PRIMARY_KEY_MULTIPLE_COLUMNS_TEST_PATTERN, changes);
                                resultString = resultString + test + "\n";
                            }
                        }
                    }
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + "MultipleColumns/" + type.getKey().toString() + "_primary_keys_test.sql");
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
        String updatePattern = "UPDATE %s SET COLUMN1=%s WHERE COLUMN1 == %s;";
        Map<String, String> result = new HashMap<String, String>();

        switch (type) {
            case SMALLINT:
                neutralValue = "8";
                firstInsertValues = createFirstValues("42", colCount);
                secondInsertValues = createSecondValues(neutralValue, firstInsertValues);
                break;
            default:
                throw new AssertionError("Unable to work with data type " + type);
        }

        assert firstInsertValues != null;
        result.put("[FIRST_INSERT]", String.format(insertPattern, tableName, listToStr(columnNames), listToStr(firstInsertValues)));
        result.put("[SECOND_INSERT]", String.format(insertPattern, tableName, listToStr(columnNames), listToStr(secondInsertValues)));
        result.put("[INVALID_UPDATE]", String.format(updatePattern, tableName, firstInsertValues.get(0), neutralValue));
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
                    "-- (Constraints - Primary keys)\n" +
                    "-- (Tests table structure:\n" +
                    "-- (Data type: [DATA_TYPE])\n" +
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
                    "-- ERROR 23505: The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by '[CONSTRAINT_NAME]' defined on '[TABLE_NAME]_[TABLE_INDEX]'.\n" +
                    "\n" +
                    "[SECOND_INSERT]\n" +
                    "-- All OK\n" +
                    "\n" +
                    "-- Updates\n" +
                    "[INVALID_UPDATE]\n" +
                    "--ERROR 23505: The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by '[CONSTRAINT_NAME]' defined on '[TABLE_NAME]_[TABLE_INDEX]'.\n" +
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
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[INDEX]' does not exist.";
}


