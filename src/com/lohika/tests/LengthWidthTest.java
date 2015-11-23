package com.lohika.tests;

import com.lohika.PatternChanger;
import com.lohika.TestPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author Pavel on 11/23/15.
 */
public class LengthWidthTest {
    private static String[] types = {"smallint"};

    private static String[] values = {"null", "1", "10"};

    private static int[] length = {1, 100, 1000000, 1000000};
    private static int[] width = {1, 5, 1012};



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
            for (int wi : width){
                for (int le : length){
                    Map<String, String> changes = new HashMap<String, String>();
                    changes.put("[TABLE_NAME]", getTableName(type));
                    changes.put("[TABLE_INDEX]", getTableIndex(wi, le));
                    changes.put("[VALUE1]", getRandomValue());
                    changes.put("[VALUE2]", getRandomValue());
                    changes.put("[WIDTH]", String.valueOf(wi));
                    changes.put("[LENGTH]", String.valueOf(le));
                    System.out.println(PatternChanger.changePattern(TestPattern.LENGTH_WIDTH_TEST_PATTERN, changes));
                }
            }
        }

    }

}
