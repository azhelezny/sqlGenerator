package com.lohika;

/**
 * @author Pavel on 11/23/15.
 */
public class TestPattern {
    public static String LENGTH_WIDTH_TEST_PATTERN =
            "-- ---------------------------------------------------------------------------\n" +
                    "-- (Width/Length)\n" +
                    "-- ([WIDTH]/[LENGTH])\n" +
                    "-- ---------------------------------------------------------------------------\n" +
                    "drop table if exists [TABLE_NAME]_[TABLE_INDEX];\n" +
                    "create table [TABLE_NAME]_[TABLE_INDEX]([COLUMNS]);\n" +
                    "\n" +
                    "-- Insert\n" +
                    "\n" +
                    "[INSERT_STATEMENT]\n" +
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
                    "[INSERT_STATEMENT]\n" +
                    "\n" +
                    "INSERT INTO [TABLE_NAME]_[TABLE_INDEX] (SELECT * FROM [TABLE_NAME]_[TABLE_INDEX]);\n" +
                    "INSERT INTO [TABLE_NAME]_[TABLE_INDEX] (SELECT * FROM [TABLE_NAME]_[TABLE_INDEX]);\n" +
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
