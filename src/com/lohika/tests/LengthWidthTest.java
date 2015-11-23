package com.lohika.tests;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel on 11/23/15.
 */
public class LengthWidthTest {
    private static String[] types = {"smallint"};

    private static String[] values = {"null", "1", "10"};

    private static int[] length = {1, 100, 1000000};
    private static int[] width = {1, 5, 1012};



    private static String getTableName(String dataType){
        return dataType + "WIDTH_LENGTH";
    }

    private static String getTableIndex(int width, int length){
        return width + "_" + length;
    }

    private static String getColumnIndex(int index){
        return String.valueOf(index);
    }


    public void createTests(){
        for (String type: types){
            for (int wi : width){
                for (int le : length){
                    Map<String, String> changes = new HashMap<String, String>();
                    changes.put("TABLE_NAME", getTableName(type));
                    changes.put("TABLE_INDEX", getTableIndex(wi, le));


                }
            }
        }

    }

}
