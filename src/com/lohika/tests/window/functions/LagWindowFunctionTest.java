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
public class LagWindowFunctionTest {

    private static String path = System.getProperty("user.dir") + "/generatedSQLs/WindowFunctions/LAG/";

    //private static List<DataType> typesToTest = new ArrayList<DataType>(Arrays.asList(new DataType[]{DataType.SMALLINT})); //new ArrayList<DataType>(Arrays.asList(DataType.getAllTypes()));
    private static List<DataType> typesToTest = new ArrayList<DataType>(Arrays.asList(DataType.getAllTypes()));


    public static String getNoWindowClauseTestCases(String tableName) {
        StringBuilder result = new StringBuilder("-- ************** OFFSET, DEFAULT VALUES AND ORDERING TESTS **************\n");
        StringBuilder positiveCases = new StringBuilder("-- *** POSITIVE CASES ***\n");
        StringBuilder negativeCases = new StringBuilder("-- *** NEGATIVE CASES ***\n");
        DefaultValueType dvt = DefaultValueType.Absent;
        for (Offset off : Offset.values())
            for (OrderBy ob : OrderBy.values()) {
                if (!off.isPositive() || dvt.equals(DefaultValueType.NotMatched)) {
                    negativeCases.append("-- (NEGATIVE) CASE: Default value type [").append(dvt.toString()).append("] Offset [").append(off.get()).append("] Ordering type [").append(ob.toString()).append("]\n");
                    negativeCases.append("SELECT column_1, LAG(column_1");
                    if (!off.equals(Offset.Absent))
                        negativeCases.append(",").append(off.get());
                    negativeCases.append(") OVER (").append(ob.get()).append(") from ").append(tableName).append(";\n\n");
                } else {
                    positiveCases.append("-- (POSITIVE) CASE: Default value type [").append(dvt.toString()).append("] Offset [").append(off.get()).append("] Ordering type [").append(ob.toString()).append("]\n");
                    positiveCases.append("SELECT column_1, LAG(column_1");
                    if (!off.equals(Offset.Absent))
                        positiveCases.append(",").append(off.get());
                    positiveCases.append(") OVER (").append(ob.get()).append(") from ").append(tableName).append(";\n\n");
                }
            }
        result.append(positiveCases).append(negativeCases);
        return result.toString();
    }

    public static String getPartitionClauses(String tableName) {
        StringBuilder result = new StringBuilder("-- ************** PARTITION BY TESTS **************\n");
        for (PartitionByType partition : PartitionByType.values())
            for (OrderBy ob : OrderBy.values()) {
                result.append("-- (POSITIVE) CASE: PARTITION TYPE [").append(partition.get()).append("]\n");
                result.append("SELECT column_1, column_2, LAG(column_1) OVER (").append(partition.get()).append(" ").append(ob.get()).append(") from ").append(tableName).append(";\n\n");
            }
        return result.toString();
    }

    public static String getRowsFrameClauses(String tableName) {
        String result = "-- ************** FRAME CLAUSE - ROWS STATEMENT TESTS **************\n";
        String pattern = "SELECT column_1, LAG(column_1) OVER(%s) FROM " + tableName + ";\n";
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
        String pattern = "SELECT column_1, LAG(column_1) OVER(%s) FROM " + tableName + ";\n";
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
                "SELECT column_1, column_2, LAG(column_1) OVER  w FROM " + tableName + " WINDOW w AS (PARTITION BY column_2 ORDER BY column_1);\n\n";

    }

    public static void createTests() {
        for (DataType type : typesToTest) {
            Map<String, String> changes = new HashMap<String, String>();
            String tableName = "LAG_" + type.toString() + "_table";
            changes.put("[DATA_TYPE]", type.toString().replace("_", " "));
            changes.put("[DROP_TABLE]", String.format("-- splicetest: ignore-output start\nDROP TABLE %s;\n-- splicetest: ignore-output stop\n", tableName));
            changes.put("[CREATE_TABLE]", getTable(new DataType[]{type, type}, tableName, 8));
            changes.put("[INSERT_DATA]", getInserts(tableName, new DataType[]{type, type}, 30));
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
