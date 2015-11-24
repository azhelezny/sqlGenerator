package com.lohika.tests;

import com.lohika.CommonPartsGeneration;
import com.lohika.FileUtils;
import com.lohika.PatternChanger;
import com.lohika.TestPattern;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Pavel on 11/23/15.
 */
public class MaxWidthTest {
    private static String path = System.getProperty("user.dir") + "/generatedSQLs/LengthTest/MaxWidth/";
    private static String[] types = {"SMALLINT", "BIGINT", "INTEGER", "REAL", "DOUBLE", "FLOAT", "DECIMAL", "NUMERIC", "DATE", "TIME", "TIMESTAMP", "CHAR", "VARCHAR", "LONG_VARCHAR", "CHAR_FOR_BIT_DATA", "VARCHAR_FOR_BIT_DATA", "LONG_VARCHAR_FOR_BIT_DATA", "BOOLEAN"};
    private static String[] values = {"null", "1", "10", "1000", "0"};
    private static int[] length = {8};
    private static int[] width = {1012};


    private static String getTableName(String dataType){
        return dataType + "_WIDTH_LENGTH";
    }

    private static String getTableIndex(int width, int length){
        return width + "_" + length;
    }

    private static String getColumnIndex(int index){
        return String.valueOf(index);
    }

    private static String getRandomValue(){
        int idx = new Random().nextInt(values.length);
        return values[idx];
    }


    public static void createTests(){
        for (String type: types){
            String resultString = "";
            Map<String, String> changes = new HashMap<String, String>();
            for (int wi : width){
                for (int le : length){
                    changes.put("[TABLE_NAME]", getTableName(type));
                    changes.put("[TABLE_INDEX]", getTableIndex(wi, le));
                    changes.put("[VALUE1]", getRandomValue());
                    changes.put("[VALUE2]", getRandomValue());
                    changes.put("[WIDTH]", String.valueOf(wi));
                    changes.put("[LENGTH]", String.valueOf(le));
                    changes.put("[DATA_TYPE]", type);
                    changes.put("[COLUMNS]", CommonPartsGeneration.generateColumnsSameType(type, wi));
                    String test = PatternChanger.changePattern(TestPattern.MAX_WIDTH_TEST_PATTERN, changes);
                    resultString = resultString + test + "\n";
                }
            }
            try {
                FileUtils.writeStringsToFile(resultString, path + type + "_max_width_test.sql");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
