package com.lohika.tests.window.functions;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.inserts.AbstractInsert;
import com.lohika.inserts.BooleanInsert;
import com.lohika.inserts.CharForBitDataInsert;
import com.lohika.inserts.CharInsert;
import com.lohika.tests.comparisons.StringComparisonOperators;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.*;

/**
 * @author Andrey Zhelezny
 *         Date: 12/7/15
 */
public class WindowFunctionsTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Comparison/";
    private static Map<DataType, DataType[]> otherTypesMapPositive = new HashMap<DataType, DataType[]>();
    private static Map<DataType, DataType[]> otherTypesMapNegative = new HashMap<DataType, DataType[]>();

    private static Set<DataType> typesToTest = new HashSet<DataType>();

    private static DataType[] arrayToType(DataType type, DataType[] types) {
        DataType[] result = new DataType[types.length + 1];
        result[0] = type;
        System.arraycopy(types, 0, result, 1, types.length + 1 - 1);
        return result;
    }

    static {
        typesToTest.add(DataType.BOOLEAN);
        typesToTest.add(DataType.CHAR_FOR_BIT_DATA);
        typesToTest.add(DataType.VARCHAR_FOR_BIT_DATA);

        otherTypesMapPositive.put(DataType.BOOLEAN, new DataType[]{DataType.BOOLEAN, DataType.BOOLEAN});
        otherTypesMapNegative.put(DataType.BOOLEAN, arrayToType(DataType.BOOLEAN, DataType.getAllTypes(DataType.BOOLEAN)));

        otherTypesMapPositive.put(DataType.CHAR_FOR_BIT_DATA, new DataType[]{DataType.CHAR_FOR_BIT_DATA, DataType.CHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA});
        otherTypesMapNegative.put(DataType.CHAR_FOR_BIT_DATA, arrayToType(DataType.CHAR_FOR_BIT_DATA, DataType.getAllTypes(DataType.CHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA)));

        otherTypesMapPositive.put(DataType.VARCHAR_FOR_BIT_DATA, new DataType[]{DataType.VARCHAR_FOR_BIT_DATA, DataType.CHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA});
        otherTypesMapNegative.put(DataType.VARCHAR_FOR_BIT_DATA, arrayToType(DataType.VARCHAR_FOR_BIT_DATA, DataType.getAllTypes(DataType.CHAR_FOR_BIT_DATA, DataType.VARCHAR_FOR_BIT_DATA)));
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

            String test1 = "";
            for (int t = 1; t < otherTypesMapPositive.get(type).length; t++) {
                DataType targetType = otherTypesMapPositive.get(type)[t];
                String tableName = type.toString() + "_VS_" + targetType.toString();
                test1 += "-- ---------- CASE: " + type.toString() + " VS " + targetType.toString() + "\n";
                test1 += "-- splicetest: ignore-output start\n";
                test1 += "DROP TABLE " + tableName + ";\n";
                test1 += "-- splicetest: ignore-output stop\n";
                test1 += getTable(new DataType[]{type, targetType}, tableName, 25) + "\n\n";
                for (int i = 0; i < 10; i++)
                    test1 += getInsert(tableName, new DataType[]{type, targetType}) + "\n";
                test1 += "\n\n";
                test1 += getOperators(2, tableName, "All OK");
                test1 += "-- splicetest: ignore-output start\n";
                test1 += "DROP TABLE " + tableName + ";\n";
                test1 += "-- splicetest: ignore-output stop\n\n";
            }


            String tableName = type.toString() + "_NEGATIVE_CHECK";

            String test3 = "-- splicetest: ignore-output start\n";
            test3 += "DROP TABLE " + tableName + ";\n";
            test3 += "-- splicetest: ignore-output stop\n\n";
            test3 += "CREATE TABLE " + tableName + " (" + CommonPartsGeneration.generateColumns(otherTypesMapNegative.get(type), "column_", 8) + ");\n\n";
            test3 += getOperators(otherTypesMapNegative.get(type).length, tableName, "ERROR 42818: Comparisons between '[DATA_TYPE]' and '[TESTING TYPE]' are not supported. Types must be comparable...");

            changes.put("[DATA_TYPE]", type.toString().replace("_", " "));
            changes.put("[TEST_1]", test1);
            changes.put("[TEST_2]", "");
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
                    "-- ********* TEST GROUP 1: 'GREEN WAY' CHECK\n" +
                    "[TEST_1]\n" +
                    "\n" +
                    "-- ********* TEST 3: NEGATIVE TESTS ON NON-SUPPORTED OPERATORS\n" +
                    "[TEST_3]\n";


}
