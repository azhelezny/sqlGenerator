package com.lohika.tests.comparisons;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.inserts.AbstractInsert;
import com.lohika.inserts.BooleanInsert;
import com.lohika.inserts.CharForBitDataInsert;
import com.lohika.inserts.CharInsert;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.*;

/**
 * @author Andrey Zhelezny
 *         Date: 12/7/15
 */
public class NegativeOtherTypesComparisonTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Comparison/";
    private static Map<DataType, DataType[]> otherTypesMapNegative = new HashMap<DataType, DataType[]>();

    private static Set<DataType> typesToTest = new HashSet<DataType>();

    private static DataType[] arrayToType(DataType type, DataType[] types) {
        DataType[] result = new DataType[types.length + 1];
        result[0] = type;
        System.arraycopy(types, 0, result, 1, types.length + 1 - 1);
        return result;
    }

    static {
        typesToTest.add(DataType.LONG_VARCHAR);
        typesToTest.add(DataType.LONG_VARCHAR_FOR_BIT_DATA);
        typesToTest.add(DataType.BLOB);
        typesToTest.add(DataType.CLOB);


        otherTypesMapNegative.put(DataType.LONG_VARCHAR, DataType.getAllTypes());
        otherTypesMapNegative.put(DataType.LONG_VARCHAR_FOR_BIT_DATA, DataType.getAllTypes());
        otherTypesMapNegative.put(DataType.BLOB, DataType.getAllTypes());
        otherTypesMapNegative.put(DataType.CLOB, DataType.getAllTypes());
    }

    private static String getOperators(int tableSize, String tableName, String message) {
        StringBuilder operators = new StringBuilder();
        for (int i = 2; i <= tableSize; i++) {
            for (StringComparisonOperators operator : StringComparisonOperators.getTwoArgsOperators()) {
                operators.append("SELECT column_1, column_").append(i).append(" FROM ").append(tableName).append(" WHERE ").append(operator.getFormat("column_1", "column_" + i).trim()).append(";\n");
                operators.append("-- ").append(message).append("\n");
            }
        }
        return operators.toString();
    }

    private static String getTable(DataType[] types, String tableName, int colCommonLength) {
        return "CREATE TABLE " + tableName + " (" + CommonPartsGeneration.generateColumns(types, "column_", colCommonLength) + ");";
    }

    private static String getInsert(String tableName, DataType[] types) {
        List<AbstractInsert> inserts = new ArrayList<AbstractInsert>();
        inserts.add(new BooleanInsert(tableName));
        inserts.add(new CharForBitDataInsert(tableName));
        inserts.add(new CharInsert(tableName));

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
                    values.append(inserts.get(0).getRandomValue()).append(", ");
                    break;
                case CHAR_FOR_BIT_DATA:
                case VARCHAR_FOR_BIT_DATA:
                case LONG_VARCHAR_FOR_BIT_DATA:
                case BLOB:
                    values.append(inserts.get(1).getRandomValue()).append(", ");
                    break;
                case LONG_VARCHAR:
                    values.append(inserts.get(2).getRandomValue()).append(", ");
                    break;
            }
        }
        values.deleteCharAt(values.length() - 1);
        values.deleteCharAt(values.length() - 1);

        return String.format(insertPattern, tableName, columns.toString(), values.toString());
    }

    public static void createTests() {
        String resultString;
        for (DataType type : typesToTest) {

            resultString = "";
            Map<String, String> changes = new HashMap<String, String>();

            String tableName = type.toString() + "_NEGATIVE_CHECK";

            String test3 = "-- splicetest: ignore-output start\n";
            test3 += "DROP TABLE " + tableName + ";\n";
            test3 += "-- splicetest: ignore-output stop\n\n";
            test3 += "CREATE TABLE " + tableName + " (" + CommonPartsGeneration.generateColumns(otherTypesMapNegative.get(type), "column_", 8) + ");\n\n";
            test3 += getOperators(otherTypesMapNegative.get(type).length, tableName, "ERROR 42818: Comparisons between '[DATA_TYPE]' and '[TESTING TYPE]' are not supported. Types must be comparable...");

            changes.put("[DATA_TYPE]", type.toString().replace("_", " "));
            changes.put("[TEST_3]", test3);
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
                    "-- ********* TEST 1: NEGATIVE TESTS ON NON-SUPPORTED OPERATORS\n" +
                    "[TEST_3]\n";


}
