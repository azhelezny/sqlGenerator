package com.lohika.tests.window.functions;

import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.types.DataType;

import java.io.IOException;
import java.util.*;

import static com.lohika.tests.window.functions.WFUtils.*;

/**
 * @author Andrey Zhelezny
 *         Date: 12/10/15
 */
@SuppressWarnings("Convert2Diamond")
public class RankWindowFunctionTest {

    private static String path = System.getProperty("user.dir") + "/generatedSQLs/WindowFunctions/RANK/";

    //private static List<DataType> typesToTest = new ArrayList<DataType>(Arrays.asList(new DataType[]{DataType.SMALLINT}));
    private static List<DataType> typesToTest = new ArrayList<DataType>(Arrays.asList(DataType.getAllTypes()));

    private static String addTestCase(String tableName, OrderBy ob, String message, PartitionByType partition) {
        StringBuilder result = new StringBuilder();
        result.append(message);
        result.append("SELECT column_1, RANK() OVER (");
        if (partition != null)
            result.append(partition.get()).append(" ");
        result.append(ob.get()).append(") from ").append(tableName).append(";\n\n");
        return result.toString();
    }

    public static String getNoWindowClauseTestCases(String tableName) {
        StringBuilder result = new StringBuilder("-- ************** ORDERING TESTS **************\n-- *** POSITIVE CASES ***\n");
        StringBuilder negativeCases = new StringBuilder("-- *** NEGATIVE CASES ***\n");
        for (OrderBy ob : OrderBy.values()) {
            if (ob.equals(OrderBy.Default))
                negativeCases.append(addTestCase(tableName, ob, "-- (NEGATIVE) CASE: Ordering type [" + ob.toString() + "]\n", null));
            else
                result.append(addTestCase(tableName, ob, "-- (POSITIVE) CASE: Ordering type [" + ob.toString() + "]\n", null));
        }
        result.append("\n").append(negativeCases);
        return result.toString();
    }

    public static String getPartitionClauses(String tableName) {
        StringBuilder result = new StringBuilder("-- ************** PARTITION BY TESTS **************\n-- *** POSITIVE CASES ***\n");
        StringBuilder negativeCases = new StringBuilder("-- *** NEGATIVE CASES ***\n");
        for (PartitionByType partition : PartitionByType.values())
            for (OrderBy ob : OrderBy.values()) {
                if (ob.equals(OrderBy.Default))
                    negativeCases.append(addTestCase(tableName, ob, "-- (NEGATIVE) CASE: PARTITION TYPE [" + partition.get() + "] Ordering type [" + ob.toString() + "]\n", partition));
                else
                    result.append(addTestCase(tableName, ob, "-- (POSITIVE) CASE: PARTITION TYPE [" + partition.get() + "] Ordering type [" + ob.toString() + "]\n", partition));
            }
        result.append("\n").append(negativeCases);
        return result.toString();
    }

    public static String getRowsFrameClauses(String tableName) {
        String result = "-- ************** FRAME CLAUSE - ROWS STATEMENT TESTS **************\n";
        String pattern = "SELECT column_1, RANK(column_1) OVER(%s) FROM " + tableName + ";\n";
        result += "-- ROWS 'Green' cases\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS 0 PRECEDING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS 1 PRECEDING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS 1 PRECEDING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS 10 PRECEDING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS UNBOUNDED PRECEDING");
        result += String.format(pattern, "PARTITION BY column_1 ORDER BY column_1 ASC NULLS FIRST ROWS 1 PRECEDING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS 0 FOLLOWING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS CURRENT ROW");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN 0 PRECEDING AND 1 FOLLOWING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN 1 PRECEDING AND 0 FOLLOWING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN 1 PRECEDING AND CURRENT ROW");
        result += String.format(pattern, "PARTITION BY column_1 ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING");
        result += "-- ROWS NEGATIVE cases\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS UNBOUNDED FOLLOWING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS 10 FOLLOWING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS -1 PRECEDING");
        result += "-- Expected ERROR 42X01: Syntax error: Encountered \"-\" at line x, column y.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS -1 FOLLOWING");
        result += "-- Expected ERROR 42X01: Syntax error: Encountered \"-\" at line x, column y.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN CURRENT ROW AND 1 PRECEDING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "PARTITION BY column_1 ORDER BY column_1 ASC NULLS FIRST ROWS 1 FOLLOWING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN 1 FOLLOWING AND 1 PRECEDING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN CURRENT ROW AND 1 PRECEDING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        return result;
    }

    public static String getRangeFrameClauses(String tableName) {
        String result = "-- ************** FRAME CLAUSE - RANGE STATEMENT TESTS **************\n";
        String pattern = "SELECT column_1, RANK(column_1) OVER(%s) FROM " + tableName + ";\n";
        result += "-- RANGE 'Green' cases\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST RANGE CURRENT ROW");
        result += String.format(pattern, "PARTITION BY column_1 ORDER BY column_1 ASC NULLS FIRST RANGE CURRENT ROW");
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING");
        result += String.format(pattern, "PARTITION BY column_1 ORDER BY column_1 ASC NULLS FIRST ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING");
        result += "-- RANGE NEGATIVE cases\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST RANGE UNBOUNDED FOLLOWING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST RANGE 10 FOLLOWING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST RANGE 10 PRECEDING");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame <n> PRECEDING of <n> FOLLOWING is only valid in ROWS mode.'.\n";
        result += String.format(pattern, "ORDER BY column_1 ASC NULLS FIRST RANGE BETWEEN UNBOUNDED FOLLOWING AND CURRENT ROW");
        result += "-- Expected ERROR XCXA0: Invalid identifier: 'Window frame end cannot precede frame start.'.\n";
        return result;
    }

    public static String getWindowClause(String tableName) {
        return "-- ************** WINDOW CLAUSE TESTS **************\n" +
                "-- (POSITIVE) CASE: Windows clause\n" +
                "SELECT column_1, column_2, RANK(column_1) OVER  w FROM " + tableName + " WINDOW w AS (ORDER BY column_2);\n\n";

    }

    public static void createTests() {
        for (DataType type : typesToTest) {
            Map<String, String> changes = new HashMap<String, String>();
            String tableName = "RANK_" + type.toString() + "_table";
            changes.put("[DATA_TYPE]", type.toString().replace("_", " "));
            changes.put("[DROP_TABLE]", String.format("-- splicetest: ignore-output start\nDROP TABLE %s;\n-- splicetest: ignore-output stop\n", tableName));
            changes.put("[CREATE_TABLE]", getTable(new DataType[]{type, type}, tableName, 8));
            changes.put("[INSERT_DATA]", getInserts(tableName, new DataType[]{type, type}, rowCount));
            changes.put("[NO_WINDOW_CLAUSE_CASES]", getNoWindowClauseTestCases(tableName));
            changes.put("[PARTITION_CLAUSE_CASES]", getPartitionClauses(tableName));
            changes.put("[FRAME_CASES_ROWS]", getRowsFrameClauses(tableName));
            changes.put("[FRAME_CASES_RANGE]", getRangeFrameClauses(tableName));
            changes.put("[WINDOW_CLAUSE_CASES]", getWindowClause(tableName));
            String test = PatternChanger.changePattern(testPattern, changes);

            try {
                FileUtils.writeStringsToFile(test, path + type.toString() + "_test.sql");
            } catch (IOException e)

            {
                e.printStackTrace();
            }
        }
    }

    public static String testPattern = "" +
            "[DROP_TABLE]\n" +
            "[CREATE_TABLE]\n" +
            "[INSERT_DATA]\n" +
            "[NO_WINDOW_CLAUSE_CASES]\n" +
            "[PARTITION_CLAUSE_CASES]\n" +
            "[FRAME_CASES_ROWS]\n" +
            "[FRAME_CASES_RANGE]\n" +
            "[WINDOW_CLAUSE_CASES]\n";
}
