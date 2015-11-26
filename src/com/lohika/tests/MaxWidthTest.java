package com.lohika.tests;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.inserts.AbstractInsert;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Pavel on 11/23/15.
 */
public class MaxWidthTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/LengthTest/MaxWidth/";
    private static DataType[] types = {DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.REAL, DataType.DOUBLE, DataType.FLOAT, DataType.DECIMAL, DataType.NUMERIC, DataType.DATE, DataType.TIME, DataType.TIMESTAMP, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR, DataType.CHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.BOOLEAN};
    private static String[] values = {"null", "1", "10", "1000", "0"};
    private final static int length[] = {8, 128, 1048576};
    private final static int width = 1012;


    private static String getTableName(String dataType) {
        return dataType + "_WIDTH_LENGTH";
    }

    private static String getTableIndex(int width, int length) {
        return width + "_" + length;
    }

    private static String getColumnIndex(int index) {
        return String.valueOf(index);
    }

    private static String getRandomValue() {
        int idx = new Random().nextInt(values.length);
        return values[idx];
    }


    public static void createTests() {
        for (DataType type : types) {
            String resultString = "";
            for (int len : length) {
                String insertsFromSelect = "";
                String tableName = getTableName(type.toString());
                String tableIndex = getTableIndex(width, len);
                AbstractInsert insert = AbstractInsert.getInsert(tableName + "_" + tableIndex, type);
                for (int i = 1; i <= getExpectedSteps(2, len); i++)
                    insertsFromSelect += insert.getInsertFromSelect() + "\n";
                Map<String, String> changes = new HashMap<String, String>();
                changes.put("[TABLE_NAME]", tableName);
                changes.put("[TABLE_INDEX]", tableIndex);
                changes.put("[VALUE1]", getRandomValue());
                changes.put("[VALUE2]", getRandomValue());
                changes.put("[WIDTH]", String.valueOf(width));
                changes.put("[LENGTH]", String.valueOf(len));
                changes.put("[DATA_TYPE]", type.toString());
                changes.put("[COLUMNS]", CommonPartsGeneration.generateColumnsSameType(type.toString(), width));
                changes.put("[INSERT_STATEMENT1]", insert.getInsertStatement(width));
                changes.put("[INSERT_STATEMENT2]", insert.getInsertStatement(width));
                changes.put("[INSERT_FROM_SELECT]", insertsFromSelect);
                String test = PatternChanger.changePattern(MAX_WIDTH_TEST_PATTERN, changes);
                resultString = resultString + test + "\n";
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + type + "_max_width_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getExpectedSteps(int startNumber, int limit) {
        int sn = startNumber, i;
        for (i = 1; i <= limit; i++)
            if (sn * 2 >= limit)
                return i;
            else
                sn = sn * 2;
        return i;
    }

    public static String MAX_WIDTH_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- [DATA_TYPE]\n" +
                    "-- (Width/Length)\n" +
                    "-- ([WIDTH]/[LENGTH])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "drop table if exists [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "create table [TABLE_NAME]_[TABLE_INDEX]([COLUMNS]);\n" +
                    "\n" +
                    "-- Insert\n" +
                    "\n" +
                    "[INSERT_STATEMENT1]\n" +
                    "[INSERT_STATEMENT2]\n" +
                    "\n" +
                    "\n" +
                    "[INSERT_FROM_SELECT]\n" +
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
}
