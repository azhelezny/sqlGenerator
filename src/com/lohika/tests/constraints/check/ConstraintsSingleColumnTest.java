package com.lohika.tests.constraints.check;

import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.TestPattern;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Pavel on 11/25/15.
 */
public class ConstraintsSingleColumnTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Constraints/";
    //private static int[] constraintColumsAffectedNumber = {1, 2, 5};

    private static String[] types = {"DATE","TIMESTAMP","TIME","SMALLINT", "INTEGER", "BIGINT", "NUMERIC", "NUMERIC(10)", "NUMERIC(10,5)", "DECIMAL", "DECIMAL(10)", "DECIMAL(10,5)", "DOUBLE", "FLOAT", "FLOAT(23)", "REAL", "BOOLEAN", "CHAR(5)", "VARCHAR(5)", "VARCHAR (1) FOR BIT DATA","CHAR (1) FOR BIT DATA"};
            //"SMALLINT", "INTEGER", "BIGINT", "NUMERIC", "NUMERIC(10)", "NUMERIC(10,5)", "DECIMAL", "DECIMAL(10)", "DECIMAL(10,5)", "DOUBLE", "FLOAT", "FLOAT(23)", "REAL", "BOOLEAN", "CHAR(5)", "VARCHAR(5)"};

    private static String[] constraintUnderTest = {"primary_key", "unique"};

    private static String[] mainConstraint = {"[CONSTRAINT NO UNDRSCORE]", "constraint [CONSTRAINT NO UNDRSCORE]", "constraint [CONSTRAINT]_main_constraint_[INDEX] [CONSTRAINT NO UNDRSCORE]"};

    private static String[] additionalConstraints = {"constraint not_null_add_[INDEX] not null ",
                                                    "not null ",
                                                    "constraint check__add_[INDEX] check ([CONSTRAINT]_column is not null) ",
                                                    "constraint check ([CONSTRAINT]_column is not null) ",
                                                    "check ([CONSTRAINT]_column is not null) ",
                                                    ""};


    private static String[] secondConstraints = {"",
                                                "primary key",
                                                "check (second_constraint_column is not null)",
                                                "not null",
                                                "unique",
                                                "constraint check__add_[INDEX] primary key",
                                                "constraint check__add_[INDEX] check (second_constraint_column is not null)",
                                                "constraint check__add_[INDEX] not null",
                                                "constraint check__add_[INDEX] unique",
                                                "constraint primary key",
                                                "constraint check (second_constraint_column is not null)",
                                                "constraint unique"};



    //method and variable will be used in case if we want to have less test cases
    private static int secondConstraintsPointer = 0;
    private static String getSecondConstraint(){
        String result;
        if (secondConstraintsPointer >= secondConstraints.length) secondConstraintsPointer=0;
        result = secondConstraints[secondConstraintsPointer];
        secondConstraintsPointer++;
        return result;
    }

    public static void createTests() {
        for (String constraint : constraintUnderTest) {
            for (String type : types) {
                int testsCount = 0;
                String resultString = "";
                Map<String, String> changes = new HashMap<String, String>();
                for (String mainConstr : mainConstraint) {
                    for (String additionalConstr : additionalConstraints) {
                        for (String secondConstr : secondConstraints) {
                            changes.put("[SECOND_CONSTRAINT]", secondConstr);
                            changes.put("[ADDITIONAL_CONSTRAINT]", additionalConstr);
                            changes.put("[MAIN_CONSTRAINT]", mainConstr);
                            changes.put("[INDEX]", String.valueOf(testsCount));
                            changes.put("[TABLE_NAME]", type.replace("(","").replace(")","").replace(",","").replace(" ","") + "_" + constraint);
                            changes.put("[DATA_TYPE]", type);
                            changes.put("[CONSTRAINT]", constraint);
                            changes.put("[CONSTRAINT NO UNDRSCORE]", constraint.replace("_"," "));

                            //Adding violation inserts (Not in pattern since not needed if no constraint)
                            if (!additionalConstr.equals("")) changes.put("[ADDITIONAL_CONSTRAINT_VIOLATION_INSERT]",
                                    "insert into [TABLE_NAME]_[INDEX] values ([ADDITIONAL_CONSTRAINT_VIOLATION], [NO_VIOLATION_SECOND_CONSTRAINT2]);");
                            else changes.put("[ADDITIONAL_CONSTRAINT_VIOLATION_INSERT]", "");

                            if (!secondConstr.equals("")) changes.put("[SECOND_CONSTRAINT_VIOLATION_INSERT]",
                                    "insert into [TABLE_NAME]_[INDEX] values ([NO_VIOLATION_MAIN_CONSTRAINT2], [SECOND_CONSTRAINT_VIOLATION]);");
                            else changes.put("[SECOND_CONSTRAINT_VIOLATION_INSERT]", "");

                            //Adding violation updates (Not in pattern since not needed if no constraint)
                            if (!additionalConstr.equals("")) changes.put("[ADDITIONAL_CONSTRAINT_VIOLATION_UPDATE]",
                                    "update [TABLE_NAME]_[INDEX] set [CONSTRAINT]_column = [ADDITIONAL_CONSTRAINT_VIOLATION] where [CONSTRAINT]_column = [NO_VIOLATION_MAIN_CONSTRAINT1];");
                            else changes.put("[ADDITIONAL_CONSTRAINT_VIOLATION_UPDATE]", "");

                            if (!secondConstr.equals("")) changes.put("[SECOND_CONSTRAINT_VIOLATION_UPDATE]",
                                    "update [TABLE_NAME]_[INDEX] set second_constraint_column = [SECOND_CONSTRAINT_VIOLATION] where second_constraint_column = [NO_VIOLATION_SECOND_CONSTRAINT1];");
                            else changes.put("[SECOND_CONSTRAINT_VIOLATION_UPDATE]", "");

                            //Adding values for data type
                            Map<String, String> values = getDataTypeValues(type);
                            Iterator it = values.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                changes.put(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
                                it.remove();
                            }
                            //Remove not violating value (violating only for unique and pk) and add violating value null
                            if (secondConstr.contains("not null") || secondConstr.contains("check (")){
                                changes.remove("[SECOND_CONSTRAINT_VIOLATION]");
                                changes.put("[SECOND_CONSTRAINT_VIOLATION]", "null");
                            }


                            String test = PatternChanger.changePattern(CONSTRAINTS_TEST_PATTERN, changes);
                            test = PatternChanger.changePattern(test, changes);
                            resultString = resultString + test + "\n";
                            testsCount++;
                        }
                    }
                }
                try {
                    FileUtils.writeStringsToFile(resultString, path + type + "_" + constraint + "_constraint_test.sql");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Tests Count: " + testsCount);
            }
        }
    }

    public static String CONSTRAINTS_TEST_PATTERN =
                    "-- ---------------------------------------------------------------------------\n" +
                    "-- (Constraints - [CONSTRAINT])\n" +
                    "-- (Tests table structure:\n" +
                    "-- create table tableN(columnN dataType additionalConstraint mainConstraint, columnN dataType secondConstraint);\n" +
                    "-- (Main constraint: [MAIN_CONSTRAINT])(Data type: [DATA_TYPE])\n" +
                    "-- (Additional constraint: [ADDITIONAL_CONSTRAINT])(Second constraint: [SECOND_CONSTRAINT])\n" +
                    "-- Test will try to add correct and violating values to all 3 constraints\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "drop table if exists [TABLE_NAME]_[INDEX];\n" +
                    "create table [TABLE_NAME]_[INDEX]([CONSTRAINT]_column [DATA_TYPE] [ADDITIONAL_CONSTRAINT][MAIN_CONSTRAINT], second_constraint_column [DATA_TYPE] [SECOND_CONSTRAINT]);\n" +
                    "\n" +
                    "-- Inserts\n" +
                    "\n" +
                    "-- No violation\n" +
                    "insert into [TABLE_NAME]_[INDEX] values ([NO_VIOLATION_MAIN_CONSTRAINT1], [NO_VIOLATION_SECOND_CONSTRAINT1]);\n" +
                    "-- Main constraint violation\n" +
                    "insert into [TABLE_NAME]_[INDEX] values ([MAIN_CONSTRAINT_VIOLATION], [NO_VIOLATION_SECOND_CONSTRAINT2]);\n" +
                    "-- Additional constraint violation\n" +
                    "[ADDITIONAL_CONSTRAINT_VIOLATION_INSERT]\n" +
                    "-- Second constraint violation\n" +
                    "[SECOND_CONSTRAINT_VIOLATION_INSERT]\n" +
                    "\n" +
                    "select * from [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- Update\n" +
                    "\n" +
                    "-- Main constraint violation\n" +
                    "update [TABLE_NAME]_[INDEX] set [CONSTRAINT]_column = [MAIN_CONSTRAINT_VIOLATION] where [CONSTRAINT]_column = [NO_VIOLATION_MAIN_CONSTRAINT1];\n" +
                    "-- Additional constraint violation\n" +
                    "[ADDITIONAL_CONSTRAINT_VIOLATION_UPDATE]\n" +
                    "-- Second constraint violation\n" +
                    "[SECOND_CONSTRAINT_VIOLATION_UPDATE]\n" +
                    "-- No violation\n" +
                    "update [TABLE_NAME]_[INDEX] set [CONSTRAINT]_column = [NO_VIOLATION_MAIN_CONSTRAINT2] where [CONSTRAINT]_column = [NO_VIOLATION_MAIN_CONSTRAINT1];\n" +
                    "update [TABLE_NAME]_[INDEX] set [CONSTRAINT]_column = [NO_VIOLATION_ADDITIONAL_CONSTRAINT] where [CONSTRAINT]_column = [NO_VIOLATION_MAIN_CONSTRAINT2];\n" +
                    "update [TABLE_NAME]_[INDEX] set second_constraint_column = [NO_VIOLATION_SECOND_CONSTRAINT2] where second_constraint_column = [NO_VIOLATION_SECOND_CONSTRAINT1];\n" +
                    "select * from [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- Drop check\n" +
                    "\n" +
                    "drop table [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- check if dropped\n" +
                    "select * from [TABLE_NAME]_[INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[INDEX]' does not exist." +
                    "\n" +
                    "\n";


    private static Map<String,String> getDataTypeValues(String type) {
        Map<String, String> result = new HashMap<String, String>();
        if (type.equalsIgnoreCase("smallint")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-32768");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "32767");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-32768");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "32767");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-32768");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-32768");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("integer")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-2147483648");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "2147483647");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-2147483648");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "2147483647");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-2147483648");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-2147483648");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("bigint")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-9223372036854775808");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "9223372036854775807");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-9223372036854775808");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "9223372036854775807");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-9223372036854775808");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-9223372036854775808");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("decimal")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "999.99");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "0.9999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "999.99");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "0.9999");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "999.99");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "999.99");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("numeric")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "999.99");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "0.9999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "999.99");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "0.9999");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "999.99");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "999.99");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("decimal(10)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "99999999.99");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "0.999999999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "99999999.99");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "0.999999999");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "99999999.99");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "99999999.99");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("numeric(10)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "99999999.99");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "0.999999999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "99999999.99");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "0.999999999");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "99999999.99");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "99999999.99");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("decimal(10,5)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "99999.99999");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "9999.99999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "99999.99999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "9999.99999");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "99999.99999");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "99999.99999");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("numeric(10,5)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "99999.99999");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "9999.99999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "99999.99999");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "9999.99999");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "0");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "99999.99999");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "99999.99999");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("double")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-1.79769E+308");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "1.79769E+308");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-1.79769E+308");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "1.79769E+308");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "2.225E-307");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-1.79769E+308");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-1.79769E+308");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("float")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-1.79769E+308");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "1.79769E+308");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-1.79769E+308");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "1.79769E+308");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "2.225E-307");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-1.79769E+308");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-1.79769E+308");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("float(23)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-3.402E+38");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "3.402E+38");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-3.402E+38");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "3.402E+38");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "1.175E-37");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-3.402E+38");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-3.402E+38");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("real")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "-3.402E+38");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "3.402E+38");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "-3.402E+38");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "3.402E+38");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "1.175E-37");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "-3.402E+38");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "-3.402E+38");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("boolean")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "true");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "false");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "true");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "false");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "true");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "true");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "true");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("char(5)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "'aaaaa'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "'bbbbb'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "'aaaaa'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "'bbbbb'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "'aabbb'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "'aaaaa'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "'aaaaa'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("char(5)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "'aaaaa'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "'bbbbb'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "'aaaaa'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "'bbbbb'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "'aabbb'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "'aaaaa'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "'aaaaa'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("varchar(5)")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "'aaaaa'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "'bbbbb'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "'aaaaa'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "'bbbbb'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "'aabbb'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "'aaaaa'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "'aaaaa'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("time")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "'15:09:02'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "'23:59:59'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "'15:09:02'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "'23:59:59'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "'00:00:00'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "'15:09:02'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "'15:09:02'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("timestamp")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "'2015-01-06 23:39:58.211'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "'2010-01-09 08:43:13.717'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "'2015-01-06 23:39:58.211'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "'2010-01-09 08:43:13.717'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "'2015-01-09 08:43:13.717'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "'2015-01-06 23:39:58.211'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "'2015-01-06 23:39:58.211'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("date")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "'2402-08-30'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "'2015-11-27'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "'2402-08-30'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "'2015-11-27'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "'2016-11-27'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "'2402-08-30'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "'2402-08-30'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }

        if (type.equalsIgnoreCase("varchar (1) for bit data")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "X'01'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "X'02'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "X'01'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "X'02'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "X'03'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "X'01'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "X'01'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }
        if (type.equalsIgnoreCase("char (1) for bit data")){
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT1]", "X'01'");
            result.put("[NO_VIOLATION_MAIN_CONSTRAINT2]", "X'02'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT1]", "X'01'");
            result.put("[NO_VIOLATION_SECOND_CONSTRAINT2]", "X'02'");
            result.put("[NO_VIOLATION_ADDITIONAL_CONSTRAINT]", "X'03'");
            result.put("[MAIN_CONSTRAINT_VIOLATION]", "X'01'");
            result.put("[SECOND_CONSTRAINT_VIOLATION]", "X'01'");
            result.put("[ADDITIONAL_CONSTRAINT_VIOLATION]", "null");
        }


        return result;
    }

}


