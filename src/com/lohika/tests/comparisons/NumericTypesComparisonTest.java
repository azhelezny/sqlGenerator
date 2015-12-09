package com.lohika.tests.comparisons;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.inserts.SmallintInsert;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.*;

/**
 * @author Andrey Zhelezny
 *         Date: 12/7/15
 */
public class NumericTypesComparisonTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Comparison/";
    private static Map<DataType, DataType[]> numTypesMapPositive = new HashMap<DataType, DataType[]>();
    private static Map<DataType, DataType[]> numTypesMapNegative = new HashMap<DataType, DataType[]>();

    private static Set<DataType> typesToTest = new HashSet<DataType>();

    private static final String leftArg = "-3142";
    private static final String rightArg = "26711";

    static {
        typesToTest.add(DataType.SMALLINT);
        typesToTest.add(DataType.INTEGER);
        typesToTest.add(DataType.BIGINT);
        typesToTest.add(DataType.DECIMAL);
        typesToTest.add(DataType.NUMERIC);
        typesToTest.add(DataType.REAL);
        typesToTest.add(DataType.DOUBLE);
        typesToTest.add(DataType.FLOAT);

        numTypesMapPositive.put(DataType.SMALLINT, new DataType[]{DataType.SMALLINT, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.INTEGER, new DataType[]{DataType.INTEGER, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.BIGINT, new DataType[]{DataType.BIGINT, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.DECIMAL, new DataType[]{DataType.DECIMAL, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.NUMERIC, new DataType[]{DataType.NUMERIC, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.REAL, new DataType[]{DataType.REAL, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.DOUBLE, new DataType[]{DataType.DOUBLE, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});
        numTypesMapPositive.put(DataType.FLOAT, new DataType[]{DataType.FLOAT, DataType.SMALLINT, DataType.INTEGER, DataType.BIGINT, DataType.DECIMAL, DataType.NUMERIC, DataType.REAL, DataType.DOUBLE, DataType.FLOAT});


        numTypesMapNegative.put(DataType.SMALLINT, new DataType[]{DataType.SMALLINT, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.INTEGER, new DataType[]{DataType.INTEGER, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.BIGINT, new DataType[]{DataType.BIGINT, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.DECIMAL, new DataType[]{DataType.DECIMAL, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.NUMERIC, new DataType[]{DataType.NUMERIC, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.REAL, new DataType[]{DataType.REAL, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.DOUBLE, new DataType[]{DataType.DOUBLE, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        numTypesMapNegative.put(DataType.FLOAT, new DataType[]{DataType.FLOAT, DataType.BOOLEAN, DataType.CHAR, DataType.VARCHAR, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA, DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.CLOB, DataType.BLOB, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
    }

    private static String getOperators(int tableSize, String tableName, boolean addInBetween, String message) {
        StringBuilder operators = new StringBuilder();
        for (int i = 2; i <= tableSize; i++) {
            for (NumericComparisonOperators operator : NumericComparisonOperators.getTwoArgsOperators()) {
                operators.append("SELECT column_1, column_").append(i).append(" FROM ").append(tableName).append(" WHERE ").append(operator.getFormat("column_1", "column_" + i).trim()).append(";\n");
                operators.append("-- ").append(message).append("\n");
            }
        }
        if (addInBetween)
            for (NumericComparisonOperators operator : NumericComparisonOperators.getThreeArgsOperators()) {
                operators.append("SELECT column_1").append(" FROM ").append(tableName).append(" WHERE ").append(operator.getFormat("column_1", leftArg, rightArg).trim()).append(";\n");
                operators.append("-- All OK\n");
            }
        return operators.toString();
    }

    public static void createTests() {
        String resultString;
        for (DataType type : typesToTest) {

            resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            String tableName = type.toString() + "_TEST";
            changes.put("[TABLE_NAME]", tableName);
            changes.put("[DATA_TYPE]", type.toString().replace("_", " "));
            changes.put("[COLUMNS_POSITIVE]", CommonPartsGeneration.generateColumns(numTypesMapPositive.get(type), "column_", -3));
            changes.put("[COLUMNS_NEGATIVE]", CommonPartsGeneration.generateColumns(numTypesMapNegative.get(type), "column_", 8));

            SmallintInsert singleIsnsert = new SmallintInsert(tableName);

            String inserts = "";
            for (int i = 0; i < 20; i++) {
                inserts += singleIsnsert.getInsertStatement(numTypesMapPositive.get(type).length) + "\n";
            }
            changes.put("[INSERT]", inserts);

            changes.put("[OPERATORS_POSITIVE]", getOperators(numTypesMapPositive.get(type).length, tableName, true, "All OK"));
            changes.put("[OPERATORS_NEGATIVE]", getOperators(numTypesMapNegative.get(type).length, tableName, false, "ERROR 42818: Comparisons between '[DATA_TYPE]' and '[TESTING TYPE]' are not supported. Types must be comparable..."));

            String test = PatternChanger.changePattern(COMPARISON_TEST_PATTERN, changes);
            resultString = resultString + test + "\n";


            try {
                FileUtils.writeStringsToFile(resultString, path + type.toString() + "_comparison_test.sql");
            } catch (IOException e)

            {
                e.printStackTrace();
            }
        }
    }

    private static String listToStr(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String listItem : list)
            result.append(listItem).append(", ");
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static String COMPARISON_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- (Target functional - Comparisons)\n" +
                    "-- (Data type: [DATA_TYPE])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "-- TEST 1: POSSIBILITY TO USE OPERATORS USED FOR CURRENT DATA TYPE\n" +
                    "-- splicetest: ignore-output start\n" +
                    "drop table if exists [TABLE_NAME];\n" +
                    "-- splicetest: ignore-output stop\n" +
                    "CREATE TABLE [TABLE_NAME]([COLUMNS_POSITIVE]);\n" +
                    "\n" +
                    "[INSERT]\n" +
                    "\n" +
                    "[OPERATORS_POSITIVE]\n" +
                    "\n" +
                    "-- TEST 2: NEGATIVE TESTS ON NON-SUPPORTED OPERATORS\n" +
                    "-- splicetest: ignore-output start\n" +
                    "drop table if exists [TABLE_NAME];\n" +
                    "-- splicetest: ignore-output stop\n" +
                    "CREATE TABLE [TABLE_NAME]([COLUMNS_NEGATIVE]);\n" +
                    "\n" +
                    "[OPERATORS_NEGATIVE]\n" +
                    "\n" +
                    "-- splicetest: ignore-output start\n" +
                    "drop table if exists [TABLE_NAME];\n" +
                    "-- splicetest: ignore-output stop\n";


}
