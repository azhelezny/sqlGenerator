package com.lohika.tests;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.TestPattern;
import com.lohika.inserts.AbstractInsert;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Pavel on 11/23/15.
 */
public class LengthWidthTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/LengthTest/LengthWidth/";
    private static Map<DataType, Integer[]> typesAndSizesPositive = new HashMap<DataType, Integer[]>();
    private static Map<DataType, Integer[]> typesAndSizesNegative = new HashMap<DataType, Integer[]>();

    static {
        typesAndSizesPositive.put(DataType.SMALLINT, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.INTEGER, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.BIGINT, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.REAL, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.DOUBLE, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.NUMERIC, new Integer[]{-1, 1, 10, 31});
        typesAndSizesPositive.put(DataType.DECIMAL, new Integer[]{-1, 1, 10, 31});
        typesAndSizesPositive.put(DataType.FLOAT, new Integer[]{-1, 1, 23, 24, 52});
        typesAndSizesPositive.put(DataType.DATE, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.TIME, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.TIMESTAMP, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.CHAR, new Integer[]{-1, 1, 10, 254});
        typesAndSizesPositive.put(DataType.CHAR_FOR_BIT_DATA, new Integer[]{-1, 1, 10, 254});
        typesAndSizesPositive.put(DataType.VARCHAR, new Integer[]{1, 10, 32672});
        typesAndSizesPositive.put(DataType.LONG_VARCHAR, new Integer[]{-1});
        typesAndSizesPositive.put(DataType.VARCHAR_FOR_BIT_DATA, new Integer[]{1, 10, 32670});
        typesAndSizesPositive.put(DataType.LONG_VARCHAR_FOR_BIT_DATA, new Integer[]{-1});

        typesAndSizesNegative.put(DataType.FLOAT, new Integer[]{-1, 0, 53});
        typesAndSizesNegative.put(DataType.NUMERIC, new Integer[]{-1, 0, 32});
        typesAndSizesNegative.put(DataType.DECIMAL, new Integer[]{-1, 0, 32});
        typesAndSizesNegative.put(DataType.CHAR, new Integer[]{-1, 0, 255});
        typesAndSizesNegative.put(DataType.CHAR_FOR_BIT_DATA, new Integer[]{-1, 0, 255});
        typesAndSizesNegative.put(DataType.VARCHAR, new Integer[]{-1, 0, 32673});
        typesAndSizesNegative.put(DataType.VARCHAR_FOR_BIT_DATA, new Integer[]{-1, 0, 32671});
    }


    private static String[] values = {"null"};
    private static int[] length = {1, 10};
    private static int[] width = {1, 5, 10};


    private static String getTableName(String dataType) {
        return dataType + "_WIDTH_LENGTH";
    }

    private static String getTableIndex(int width, int length) {
        return width + "_" + length;
    }

    private static String getTableIndex(int width, int length, int colSize) {
        return width + "_" + length + "_WITH_COLUMN_SIZE_" + ((colSize >= 0) ? colSize : "MINUS_" + colSize);
    }


    private static String getRandomValue() {
        int idx = new Random().nextInt(values.length);
        return values[idx];
    }


    public static void createPositiveTests() {
        for (Map.Entry<DataType, Integer[]> type : typesAndSizesPositive.entrySet()) {
            String resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            for (int wi : width) {
                for (int le : length) {
                    for (int colSize : type.getValue()) {
                        String columns, size = "DEFAULT", tableIndex, tableName = getTableName(type.getKey().toString());
                        String insertStatement = "";
                        AbstractInsert insert;
                        if (colSize == -1) {
                            tableIndex = getTableIndex(wi, le);
                            columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, -101);
                            insert = AbstractInsert.getInsert(tableName + "_" + tableIndex, type.getKey());
                            for (int i = 0; i < le; i++)
                                insertStatement += insert.getInsertStatement(wi) + "\n";
                        } else {
                            tableIndex = getTableIndex(wi, le, colSize);
                            columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, colSize);
                            size = String.valueOf(colSize);
                            insert = AbstractInsert.getInsert(tableName + "_" + tableIndex, type.getKey(), colSize);
                            for (int i = 0; i < le; i++)
                                insertStatement += insert.getInsertStatement(wi) + "\n";
                        }

                        changes.put("[TABLE_NAME]", tableName);
                        changes.put("[TABLE_INDEX]", tableIndex);
                        changes.put("[SIZE]", size);
                        changes.put("[VALUE1]", getRandomValue());
                        changes.put("[VALUE2]", getRandomValue());
                        changes.put("[WIDTH]", String.valueOf(wi));
                        changes.put("[INSERT_STATEMENT]", insertStatement);
                        changes.put("[LENGTH]", String.valueOf(le));
                        changes.put("[DATA_TYPE]", type.getKey().toString());
                        changes.put("[COLUMNS]", columns);
                        String test = PatternChanger.changePattern(LENGTH_WIDTH_POSITIVE_TEST_PATTERN, changes);
                        resultString = resultString + test + "\n";
                    }
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + "POSITIVE/" + type.getKey().toString() + "_width_length_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createNegativeTests() {
        for (Map.Entry<DataType, Integer[]> type : typesAndSizesNegative.entrySet()) {
            String resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            for (int wi : width) {
                for (int le : length) {
                    for (int colSize : type.getValue()) {
                        String columns, tableIndex, tableName = getTableName(type.getKey().toString()), errorMessage;
                        if (colSize == -1)
                            errorMessage = "ERROR 42X01: Syntax error: Encountered \"-\" at line [x], column [y].";
                        else
                            errorMessage = "Invalid length '[x]' in column specification.";
                        tableIndex = getTableIndex(wi, le, colSize);
                        columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, colSize);
                        changes.put("[TABLE_NAME]", tableName);
                        changes.put("[TABLE_INDEX]", tableIndex);
                        changes.put("[SIZE]", String.valueOf(colSize));
                        changes.put("[WIDTH]", String.valueOf(wi));
                        changes.put("[LENGTH]", String.valueOf(le));
                        changes.put("[DATA_TYPE]", type.getKey().toString());
                        changes.put("[COLUMNS]", columns);
                        changes.put("[ERROR_MESSAGE]", errorMessage);
                        String test = PatternChanger.changePattern(LENGTH_WIDTH_NEGATIVE_CREATION_TEST_PATTERN, changes);
                        resultString = resultString + test + "\n";
                    }
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + "NEGATIVE/CREATION/" + type.getKey().toString() + "_width_length_negative_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createNegativeInsertUpdateTests() {
        for (Map.Entry<DataType, Integer[]> type : typesAndSizesNegative.entrySet()) {
            String resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            for (int wi : width) {
                for (int colSize : type.getValue()) {
                    String columns, tableIndex, tableName = getTableName(type.getKey().toString()), errorMessage;
                    if (colSize == -1) {
                        tableIndex = getTableIndex(wi, 0);
                        columns = CommonPartsGeneration.generateColumnsSameType(type.getKey().toString(), wi);
                        errorMessage = "ERROR 42X01: Syntax error: Encountered \"-\" at line [x], column [y].";

                    } else {
                        tableIndex = getTableIndex(wi, 0, colSize);
                        columns = CommonPartsGeneration.generateColumnsSameType(type.getKey(), wi, colSize);
                        errorMessage = "Invalid length '[x]' in column specification.";
                    }

                    changes.put("[TABLE_NAME]", tableName);
                    changes.put("[TABLE_INDEX]", tableIndex);
                    changes.put("[SIZE]", String.valueOf(colSize));
                    changes.put("[WIDTH]", String.valueOf(wi));
                    changes.put("[DATA_TYPE]", type.getKey().toString());
                    changes.put("[COLUMNS]", columns);
                    changes.put("[ERROR_MESSAGE]", errorMessage);
                    String test = PatternChanger.changePattern(LENGTH_WIDTH_NEGATIVE_CREATION_TEST_PATTERN, changes);
                    resultString = resultString + test + "\n";
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + "NEGATIVE/CREATION/" + type.getKey().toString() + "_width_length_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String LENGTH_WIDTH_POSITIVE_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- (Width/Length/Size)\n" +
                    "-- ([WIDTH]/[LENGTH]/[SIZE])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "drop table if exists [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "create table [TABLE_NAME]_[TABLE_INDEX]([COLUMNS]);\n" +
                    "\n" +
                    "-- Insert\n" +
                    "\n" +
                    "[INSERT_STATEMENT]\n" +
                    "\n" +
                    "-- DML part\n" +
                    "\n" +
                    "select * from [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "\n" +
                    "-- Update\n" +
                    "\n" +
                    "update [TABLE_NAME]_[TABLE_INDEX] set column_1 = [VALUE1] where column_1 = [VALUE2];\n" +
                    "select * from [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "\n" +
                    "-- Drop check\n" +
                    "\n" +
                    "drop table [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "\n" +
                    "-- check if dropped\n" +
                    "select * from [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[TABLE_INDEX]' does not exist.";

    public static String LENGTH_WIDTH_NEGATIVE_CREATION_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- (Width/Size)\n" +
                    "-- ([WIDTH]/[SIZE])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "drop table if exists [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "create table [TABLE_NAME]_[TABLE_INDEX]([COLUMNS]);\n" +
                    "-- [ERROR_MESSAGE]\n" +
                    "\n" +
                    "drop table [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[TABLE_INDEX]' does not exist.\n" +
                    "\n" +
                    "-- check if dropped\n" +
                    "select * from [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[TABLE_INDEX]' does not exist.";
}
