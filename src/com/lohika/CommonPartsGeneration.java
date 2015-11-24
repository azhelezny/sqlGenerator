package com.lohika;

/**
 * @author Pavel on 11/24/15.
 */
public class CommonPartsGeneration {
    public static String generateColumnsSameType(String type, int columnsNumber){
        String result = "";
        for (int i = 1; i<=columnsNumber; i++){
            if (i == 1){
                result = result + "column_" + i + " " + type;
            }
            else {
                result = result + ", column_" + i + " " + type;
            }

        }
        return result;
    }
}
