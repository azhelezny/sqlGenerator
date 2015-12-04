package com.lohika.tests.functions.aggregate;

import com.lohika.FileUtils;
import com.lohika.PatternChanger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Pavel on 12/1/15.
 */
public class SingleColumnTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Functions/Aggregate/";
    private static String[] types = {"LONG VARCHAR","VARCHAR (1) FOR BIT DATA","CHAR (1) FOR BIT DATA"};
    //"SMALLINT", "INT", "BIGINT", "NUMERIC", "NUMERIC(10)", "NUMERIC(10,5)", "DECIMAL", "DECIMAL(10)", "DECIMAL(10,5)", "DOUBLE", "FLOAT", "FLOAT(23)", "REAL", "BOOLEAN", "CHAR(5)", "VARCHAR(5)"};
    //{"DATE","TIMESTAMP","TIME"};
    //{"LONG VARCHAR","VARCHAR (1) FOR BIT DATA","CHAR (1) FOR BIT DATA"};
    private static String[] functions = {"MAX", "MIN", "COUNT", "SUM", "AVG"};

    private static String[] expressions = {"column1", "column1 + 0", "column1/100", "column1 * 1", "column1 - 0", "LENGTH(column1)"};
    private static String[] distinct = {"","DISTINCT "};

    public static void createTests() {
        for (String type : types) {
            String resultString = "";
            int testsCount = 0;
            Map<String, String> changes = new HashMap<String, String>();
            for (String function : functions) {
                for (String expression : expressions) {
                    for (String  dist : distinct) {
                        changes.put("[INDEX]", String.valueOf(testsCount));
                        changes.put("[TABLE_NAME]", type.replace("(","").replace(")","").replace(",","").replace(" ","")  + "_AGGREGATE_FUNCTIONS");
                        changes.put("[FUNCTION]", function);
                        changes.put("[DISTINCT]", dist);
                        changes.put("[EXPRESSION]", expression);
                        changes.put("[DATA_TYPE]", type);

                        //Adding values for data type
                        Map<String, String> values = getDataTypeValues(type);
                        Iterator it = values.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            changes.put(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
                            it.remove();
                        }


                        String test = "";
                        if (testsCount == 0){
                            test = FUNCTIONS_SINGLE_COLUMN_DDL_PATTERN;
                        }

                        test = PatternChanger.changePattern(test + FUNCTIONS_SINGLE_COLUMN_TEST_PATTERN, changes);
                        test = PatternChanger.changePattern(test, changes);
                        resultString = resultString + test + "\n";
                        testsCount++;
                    }
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + type + "_aggregate_functions_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Tests Count: " + testsCount);
        }
    }





    public static String FUNCTIONS_SINGLE_COLUMN_DDL_PATTERN = "drop table if exists [TABLE_NAME];\n" +
                    "create table [TABLE_NAME](column1 [DATA_TYPE]);\n" +
                    "-- Inserts\n" +
                    "insert into [TABLE_NAME] (column1) values [MAX_VALUE], [MIN_VALUE], null, [SOME_VALUE];\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "insert into [TABLE_NAME] (select * from [TABLE_NAME]);\n" +
                    "\n" +
                    "\n";

    public static String FUNCTIONS_SINGLE_COLUMN_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- (Function - [FUNCTION])\n" +
                    "-- select function(distinct? expression|column);\n" +
                    "-- (Distinct: [DISTINCT])(Data type: [DATA_TYPE])(Expression: [EXPRESSION])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "select [FUNCTION]([DISTINCT][EXPRESSION]) from [TABLE_NAME];\n" +
                    "-- Expected output:" +
                    "\n" +
                    "\n";




    private static Map<String,String> getDataTypeValues(String type) {
        Map<String, String> result = new HashMap<String, String>();
        if (type.equalsIgnoreCase("smallint")){
            result.put("[MIN_VALUE]", "-32768");
            result.put("[MAX_VALUE]", "32767");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("integer")){
            result.put("[MIN_VALUE]", "-2147483648");
            result.put("[MAX_VALUE]", "2147483647");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("bigint")){
            result.put("[MIN_VALUE]", "-9223372036854775808");
            result.put("[MAX_VALUE]", "9223372036854775807");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("decimal")){
            result.put("[MIN_VALUE]", "999.99");
            result.put("[MAX_VALUE]", "0.9999");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("numeric")){
            result.put("[MIN_VALUE]", "999.99");
            result.put("[MAX_VALUE]", "0.9999");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("decimal(10)")){
            result.put("[MIN_VALUE]", "99999999.99");
            result.put("[MAX_VALUE]", "0.999999999");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("numeric(10)")){
            result.put("[MIN_VALUE]", "99999999.99");
            result.put("[MAX_VALUE]", "0.999999999");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("decimal(10,5)")){
            result.put("[MIN_VALUE]", "99999.99999");
            result.put("[MAX_VALUE]", "00000.00001");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("numeric(10,5)")){
            result.put("[MIN_VALUE]", "99999.99999");
            result.put("[MAX_VALUE]", "00000.00001");
            result.put("[SOME_VALUE]", "0");
        }
        if (type.equalsIgnoreCase("double")){
            result.put("[MIN_VALUE]", "-1.79769E+308");
            result.put("[MAX_VALUE]", "1.79769E+308");
            result.put("[SOME_VALUE]", "2.225E-307");
        }
        if (type.equalsIgnoreCase("float")){
            result.put("[MIN_VALUE]", "-1.79769E+308");
            result.put("[MAX_VALUE]", "1.79769E+308");
            result.put("[SOME_VALUE]", "2.225E-307");
        }
        if (type.equalsIgnoreCase("float(23)")){
            result.put("[MIN_VALUE]", "-3.402E+38");
            result.put("[MAX_VALUE]", "3.402E+38");
            result.put("[SOME_VALUE]", "1.175E-37");
        }
        if (type.equalsIgnoreCase("real")){
            result.put("[MIN_VALUE]", "-3.402E+38");
            result.put("[MAX_VALUE]", "3.402E+38");
            result.put("[SOME_VALUE]", "1.175E-37");
        }
        if (type.equalsIgnoreCase("boolean")){
            result.put("[MIN_VALUE]", "true");
            result.put("[MAX_VALUE]", "false");
            result.put("[SOME_VALUE]", "true");
        }
        if (type.equalsIgnoreCase("char(5)")){
            result.put("[MIN_VALUE]", "'aaaaa'");
            result.put("[MAX_VALUE]", "'bbbbb'");
            result.put("[SOME_VALUE]", "'aabbb'");
        }
        if (type.equalsIgnoreCase("char(5)")){
            result.put("[MIN_VALUE]", "'aaaaa'");
            result.put("[MAX_VALUE]", "'bbbbb'");
            result.put("[SOME_VALUE]", "'aabbb'");
        }
        if (type.equalsIgnoreCase("varchar(5)")){
            result.put("[MIN_VALUE]", "'aaaaa'");
            result.put("[MAX_VALUE]", "'bbbbb'");
            result.put("[SOME_VALUE]", "'aabbb'");
        }
        if (type.equalsIgnoreCase("time")){
            result.put("[MIN_VALUE]", "'15:09:02'");
            result.put("[MAX_VALUE]", "'23:59:59'");
            result.put("[SOME_VALUE]", "'00:00:00'");
        }
        if (type.equalsIgnoreCase("timestamp")){
            result.put("[MIN_VALUE]", "'2015-01-06 23:39:58.211'");
            result.put("[MAX_VALUE]", "'2010-01-09 08:43:13.717'");
            result.put("[SOME_VALUE]", "'2015-01-09 08:43:13.717'");
        }
        if (type.equalsIgnoreCase("date")){
            result.put("[MIN_VALUE]", "'2402-08-30'");
            result.put("[MAX_VALUE]", "'2015-11-27'");
            result.put("[SOME_VALUE]", "'2016-11-27'");
        }
        if (type.equalsIgnoreCase("long varchar")){
            result.put("[MIN_VALUE]", "'a'");
            result.put("[MAX_VALUE]", "'bbb'");
            result.put("[SOME_VALUE]", "'aabb'");
        }
        if (type.equalsIgnoreCase("varchar (1) for bit data")){
            result.put("[MIN_VALUE]", "X'01'");
            result.put("[MAX_VALUE]", "X'02'");
            result.put("[SOME_VALUE]", "X'03'");
        }
        if (type.equalsIgnoreCase("char (1) for bit data")){
            result.put("[MIN_VALUE]", "X'01'");
            result.put("[MAX_VALUE]", "X'02'");
            result.put("[SOME_VALUE]", "X'03'");
        }

        return result;
    }
}
