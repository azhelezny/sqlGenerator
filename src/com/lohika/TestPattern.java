package com.lohika;

/**
 * @author Pavel on 11/23/15.
 */
public class TestPattern {
    public static String LENGTH_WIDTH_TEST_PATTERN =
                "-- ---------------------------------------------------------------------------\n" +
                "-- (Width/Length)\n" +
                "-- (WIDTH/LENGTH)\n" +
                "-- ---------------------------------------------------------------------------" +
                "drop table if exists TABLE_NAME_TABLE_INDEX;\n" +
                "create table TABLE_NAME_INDEX(column_COLUMN_INDEX DATA_TYPE);\n" +
                "\n" +
                "-- Insert\n" +
                "\n" +
                "INSERT_STATEMENT\n" +
                "\n" +
                "-- DML part\n" +
                "\n" +
                "select * from TABLE_NAME_TABLE_INDEX;\n" +
                "\n" +
                "-- Update\n" +
                "\n" +
                "update TABLE_NAME_TABLE_INDEX set column_COLUMN_INDEX = VALUE where column_COLUMN_INDEX = VALUE\n" +
                "select * from TABLE_NAME_TABLE_INDEX;\n" +
                "\n" +
                "-- Drop check\n" +
                "\n" +
                "drop table TABLE_NAME_TABLE_INDEX;\n" +
                "\n" +
                "-- check if dropped\n" +
                "select * from TABLE_NAME_TABLE_INDEX;\n" +
                "-- ERROR 42X05: Table/View 'TABLE_NAME_TABLE_INDEX' does not exist.";
}
