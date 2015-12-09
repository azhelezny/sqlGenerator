package com.lohika.tests.comparisons;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.inserts.*;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.*;

/**
 * @author Andrey Zhelezny
 *         Date: 12/7/15
 */
public class OtherTypesComparisonTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Comparison/";
    private static Map<DataType, DataType[]> charTypesMapPositive = new HashMap<DataType, DataType[]>();
    private static Map<DataType, DataType[]> charTypesMapNegativeFormat = new HashMap<DataType, DataType[]>();
    private static Map<DataType, DataType[]> charTypesMapNegative = new HashMap<DataType, DataType[]>();

    private static Set<DataType> typesToTest = new HashSet<DataType>();

    private static DataType[] arrayToType(DataType type, DataType[] types) {
        DataType[] result = new DataType[types.length + 1];
        result[0] = type;
        System.arraycopy(types, 0, result, 1, types.length + 1 - 1);
        return result;
    }

    static {
        typesToTest.add(DataType.CHAR);
        typesToTest.add(DataType.VARCHAR);
        typesToTest.add(DataType.DATE);
        typesToTest.add(DataType.TIME);
        typesToTest.add(DataType.TIMESTAMP);

        charTypesMapPositive.put(DataType.CHAR, new DataType[]{DataType.CHAR, DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        charTypesMapNegativeFormat.put(DataType.CHAR, new DataType[]{DataType.CHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        charTypesMapNegative.put(DataType.CHAR, arrayToType(DataType.CHAR, DataType.getAllTypes(DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP)));

        charTypesMapPositive.put(DataType.VARCHAR, new DataType[]{DataType.VARCHAR, DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        charTypesMapNegativeFormat.put(DataType.VARCHAR, new DataType[]{DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP});
        charTypesMapNegative.put(DataType.VARCHAR, arrayToType(DataType.VARCHAR, DataType.getAllTypes(DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP)));

        charTypesMapPositive.put(DataType.DATE, new DataType[]{DataType.DATE, DataType.CHAR, DataType.VARCHAR, DataType.DATE});
        charTypesMapNegativeFormat.put(DataType.DATE, new DataType[]{DataType.DATE, DataType.VARCHAR, DataType.CHAR});
        charTypesMapNegative.put(DataType.DATE, arrayToType(DataType.DATE, DataType.getAllTypes(DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP)));

        charTypesMapPositive.put(DataType.TIME, new DataType[]{DataType.TIME, DataType.CHAR, DataType.VARCHAR, DataType.TIME});
        charTypesMapNegativeFormat.put(DataType.TIME, new DataType[]{DataType.TIME, DataType.VARCHAR, DataType.CHAR});
        charTypesMapNegative.put(DataType.TIME, arrayToType(DataType.TIME, DataType.getAllTypes(DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP)));

        charTypesMapPositive.put(DataType.TIMESTAMP, new DataType[]{DataType.TIMESTAMP, DataType.CHAR, DataType.VARCHAR, DataType.TIMESTAMP});
        charTypesMapNegativeFormat.put(DataType.TIMESTAMP, new DataType[]{DataType.TIMESTAMP, DataType.VARCHAR, DataType.CHAR});
        charTypesMapNegative.put(DataType.TIMESTAMP, arrayToType(DataType.TIMESTAMP, DataType.getAllTypes(DataType.CHAR, DataType.VARCHAR, DataType.DATE, DataType.TIME, DataType.TIMESTAMP)));
    }

    private static String getOperators(int tableSize, String tableName, String message, boolean useAlt) {
        StringBuilder operators = new StringBuilder();
        for (int i = 2; i <= tableSize; i++) {
            for (StringComparisonOperators operator : StringComparisonOperators.getTwoArgsOperators()) {
                operators.append("SELECT column_1, column_").append(i).append(" FROM ").append(tableName).append(" WHERE ").append(operator.getFormat("column_1", "column_" + i).trim()).append(";\n");
                operators.append("-- ").append(message).append("\n");
            }
        }
        if (useAlt)
            for (StringComparisonOperators operator : StringComparisonOperators.getSpecialOperators()) {
                operators.append("SELECT column_1").append(" FROM ").append(tableName).append(" WHERE ").append(operator.getFormat("column_1", "%").trim()).append(";\n");
                operators.append("-- All OK\n");
            }
        return operators.toString();
    }

    private static String getTable(DataType[] types, String tableName, int colCommonLength) {
        return "CREATE TABLE " + tableName + " (" + CommonPartsGeneration.generateColumns(types, "column_", colCommonLength) + ");";
    }

    private static DataType findFirstNotChar(DataType[] types) {
        for (DataType type : types)
            if (!type.equals(DataType.VARCHAR) && !type.equals(DataType.CHAR))
                return type;
        return DataType.CHAR;
    }

    private static String getInsert(String tableName, DataType[] types, boolean compatibilityMode) {
        List<AbstractInsert> inserts = new ArrayList<AbstractInsert>();
        inserts.add(new DateInsert(tableName));
        inserts.add(new TimeInsert(tableName));
        inserts.add(new TimestampInsert(tableName));
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

            if (compatibilityMode)
                if (type.equals(DataType.CHAR) || type.equals(DataType.VARCHAR))
                    type = findFirstNotChar(types);

            assert type != null;
            switch (type) {
                case CHAR:
                case VARCHAR:
                    values.append(inserts.get(3).getRandomValue()).append(", ");
                    break;
                case DATE:
                    values.append(inserts.get(0).getRandomValue()).append(", ");
                    break;
                case TIME:
                    values.append(inserts.get(1).getRandomValue()).append(", ");
                    break;
                case TIMESTAMP:
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
            for (int t = 1; t < charTypesMapPositive.get(type).length; t++) {
                DataType targetType = charTypesMapPositive.get(type)[t];
                String tableName = type.toString() + "_VS_" + targetType.toString();
                test1 += "-- ---------- CASE: " + type.toString() + " VS " + targetType.toString() + "\n";
                test1 += "-- splicetest: ignore-output start\n";
                test1 += "DROP TABLE " + tableName + ";\n";
                test1 += "-- splicetest: ignore-output stop\n";
                test1 += getTable(new DataType[]{type, targetType}, tableName, 25) + "\n\n";
                for (int i = 0; i < 10; i++)
                    test1 += getInsert(tableName, new DataType[]{type, targetType}, true) + "\n";
                test1 += "\n\n";
                test1 += getOperators(2, tableName, "All OK", (type.equals(DataType.CHAR) || (type.equals(DataType.VARCHAR))));
                test1 += "-- splicetest: ignore-output start\n";
                test1 += "DROP TABLE " + tableName + ";\n";
                test1 += "-- splicetest: ignore-output stop\n\n";

            }

            String test2 = "";
            for (int t = 1; t < charTypesMapNegativeFormat.get(type).length; t++) {
                DataType targetType = charTypesMapNegativeFormat.get(type)[t];
                String tableName = type.toString() + "_VS_" + targetType.toString();
                test2 += "-- ---------- CASE: " + type.toString() + " VS " + targetType.toString() + "\n";
                test2 += "-- splicetest: ignore-output start\n";
                test2 += "DROP TABLE " + tableName + ";\n";
                test2 += "-- splicetest: ignore-output stop\n";
                test2 += getTable(new DataType[]{type, targetType}, tableName, 25) + "\n\n";
                for (int i = 0; i < 10; i++)
                    test2 += getInsert(tableName, new DataType[]{type, targetType}, false) + "\n";
                test2 += "\n\n";
                test2 += getOperators(2, tableName, "ERROR 22007: The syntax of the string representation of a datetime value is incorrect.", false);
                test2 += "-- splicetest: ignore-output start\n";
                test2 += "DROP TABLE " + tableName + ";\n";
                test2 += "-- splicetest: ignore-output stop\n\n";

            }

            String tableName = type.toString() + "_NEGATIVE_CHECK";
            test2 += "-- splicetest: ignore-output start\n";
            test2 += "DROP TABLE " + tableName + ";\n";
            test2 += "-- splicetest: ignore-output stop\n";

            String test3 = "CREATE TABLE " + tableName + " (" + CommonPartsGeneration.generateColumns(charTypesMapNegative.get(type), "column_", 8) + ");\n\n";
            test3 += getOperators(charTypesMapNegative.get(type).length, tableName, "ERROR 42818: Comparisons between '[DATA_TYPE]' and '[TESTING TYPE]' are not supported. Types must be comparable...", false);

            // changes.put("[COLUMNS_NEGATIVE]", CommonPartsGeneration.generateColumns(numTypesMapNegative.get(type), "column_", 8));
            // changes.put("[OPERATORS_NEGATIVE]", getOperators(numTypesMapNegative.get(type).length, tableName, false, "ERROR 42818: Comparisons between '[DATA_TYPE]' and '[TESTING TYPE]' are not supported. Types must be comparable..."));

            changes.put("[DATA_TYPE]", type.toString().replace("_", " "));
            changes.put("[TEST_1]", test1);
            changes.put("[TEST_2]", test2);
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
                    "-- ********* TEST GROUP 2: FORMAT ERROR - CASE WHEN TWO TYPES ARE COMPARABLE BUT VALUE IN FIRST COLUMN CANNOT BE CAST TO TYPE OF SECOND\n" +
                    "[TEST_2]\n" +
                    "\n" +
                    "-- ********* TEST 3: NEGATIVE TESTS ON NON-SUPPORTED OPERATORS\n" +
                    "[TEST_3]\n";


}
