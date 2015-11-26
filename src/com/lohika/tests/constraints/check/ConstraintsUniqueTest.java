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
public class ConstraintsUniqueTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/Constraints/";
    //private static int[] constraintColumsAffectedNumber = {1, 2, 5};

    private static String[] types = {"SMALLINT"};

    private static String[] mainConstraint = {"unique", "constraint unique", "constraint unique_main_constraint_[INDEX] unique"};

    private static String[] additionalConstraints = {"constraint not_null_add_[INDEX] not null ",
                                                    "constraint not null ",
                                                    "not null ",
                                                    "constraint check__add_[INDEX] check (unique_column is not null) ",
                                                    "constraint check (unique_column is not null) ",
                                                    "check (unique_column is not null) ",
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
                                                "constraint not null",
                                                "constraint unique"};

    private static int secondConstraintsPointer = 0;

    private static String getSecondConstraint(){
        String result;
        if (secondConstraintsPointer >= secondConstraints.length) secondConstraintsPointer=0;
        result = secondConstraints[secondConstraintsPointer];
        secondConstraintsPointer++;
        return result;
    }

    public static void createTests(){
        int testsCount = 0;
        for (String type: types) {
            String resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            for (String mainConstr : mainConstraint) {
                for (String additionalConstr : additionalConstraints) {
                    changes.put("[SECOND_CONSTRAINT]", getSecondConstraint());
                    changes.put("[ADDITIONAL_CONSTRAINT]", additionalConstr);
                    changes.put("[MAIN_CONSTRAINT]", mainConstr);
                    changes.put("[INDEX]", String.valueOf(testsCount));
                    changes.put("[TABLE_NAME]", type + "_UNIQUE_");
                    changes.put("[DATA_TYPE]", type);
                    //Adding values for data type
                    Map<String, String> values = getDataTypeValues(type);
                    Iterator it = values.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        changes.put(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
                        it.remove();
                    }

                    String test = PatternChanger.changePattern(CONSTRAINTS_UNIQUE_TEST_PATTERN, changes);
                    test = PatternChanger.changePattern(test, changes);
                    resultString = resultString + test + "\n";
                    testsCount++;
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + type + "_unique_constraint_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Tests Count: " + testsCount);
    }

    public static String CONSTRAINTS_UNIQUE_TEST_PATTERN =
                    "-- ---------------------------------------------------------------------------\n" +
                    "-- (Constraints - UNIQUE)\n" +
                    "-- (Tests table structure:\n" +
                    "-- create table tableN(columnN dataType additionalConstraint mainConstraint, columnN dataType secondConstraint);\n" +
                    "-- (Main constraint: [MAIN_CONSTRAINT])(Data type: [DATA_TYPE])\n" +
                    "-- (Additional constraint: [ADDITIONAL_CONSTRAINT])(Second constraint: [SECOND_CONSTRAINT])\n" +
                    "-- Test will try to add correct and violating values to all 3 constraints\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "drop table if exists [TABLE_NAME]_[INDEX];\n" +
                    "create table [TABLE_NAME]_[INDEX](unique_column [DATA_TYPE] [ADDITIONAL_CONSTRAINT][MAIN_CONSTRAINT], second_constraint_column [DATA_TYPE] [SECOND_CONSTRAINT]);\n" +
                    "\n" +
                    "-- Inserts\n" +
                    "\n" +
                    "-- No violation\n" +
                    "insert into [TABLE_NAME]_[INDEX] values ([NO_VIOLATION_MAIN_CONSTRAINT1], [NO_VIOLATION_SECOND_CONSTRAINT1]);\n" +
                    "-- Main constraint violation\n" +
                    "insert into [TABLE_NAME]_[INDEX] values ([MAIN_CONSTRAINT_VIOLATION], [NO_VIOLATION_SECOND_CONSTRAINT2]);\n" +
                    "-- Additional constraint violation\n" +
                    "insert into [TABLE_NAME]_[INDEX] values ([ADDITIONAL_CONSTRAINT_VIOLATION], [NO_VIOLATION_SECOND_CONSTRAINT2]);\n" +
                    "-- Second constraint violation\n" +
                    "insert into [TABLE_NAME]_[INDEX] values ([NO_VIOLATION_MAIN_CONSTRAINT2], [SECOND_CONSTRAINT_VIOLATION]);\n" +
                    "\n" +
                    "select * from [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- Update\n" +
                    "\n" +
                    "-- Main constraint violation\n" +
                    "update [TABLE_NAME]_[INDEX] set unique_column = [MAIN_CONSTRAINT_VIOLATION] where unique_column = [NO_VIOLATION_MAIN_CONSTRAINT1];\n" +
                    "-- Additional constraint violation\n" +
                    "update [TABLE_NAME]_[INDEX] set unique_column = [ADDITIONAL_CONSTRAINT_VIOLATION] where unique_column = [NO_VIOLATION_MAIN_CONSTRAINT1];\n" +
                    "-- Second constraint violation\n" +
                    "update [TABLE_NAME]_[INDEX] set second_constraint_column = [SECOND_CONSTRAINT_VIOLATION] where second_constraint_column = [NO_VIOLATION_SECOND_CONSTRAINT1];\n" +
                    "-- No violation\n" +
                    "update [TABLE_NAME]_[INDEX] set unique_column = [NO_VIOLATION_MAIN_CONSTRAINT2] where unique_column = [NO_VIOLATION_MAIN_CONSTRAINT1];\n" +
                    "update [TABLE_NAME]_[INDEX] set unique_column = [NO_VIOLATION_ADDITIONAL_CONSTRAINT] where unique_column = [NO_VIOLATION_MAIN_CONSTRAINT2];\n" +
                    "update [TABLE_NAME]_[INDEX] set second_constraint_column = [NO_VIOLATION_SECOND_CONSTRAINT2] where second_constraint_column = [NO_VIOLATION_SECOND_CONSTRAINT1];\n" +
                    "select * from [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- Drop check\n" +
                    "\n" +
                    "drop table [TABLE_NAME]_[INDEX];\n" +
                    "\n" +
                    "-- check if dropped\n" +
                    "select * from [TABLE_NAME]_[INDEX];\n" +
                    "-- ERROR 42X05: Table/View '[TABLE_NAME]_[INDEX]' does not exist.";


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

        return result;
    }

}


