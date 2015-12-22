package com.lohika.tests.comparisons;

import com.lohika.types.DataType;

/**
 * @author Andrey Zhelezny
 *         Date: 12/18/15
 */
public class ComparisonUtils {
    public static DataType[] arrayToType(DataType type, DataType[] types) {
        DataType[] result = new DataType[types.length + 1];
        result[0] = type;
        System.arraycopy(types, 0, result, 1, types.length + 1 - 1);
        return result;
    }
}
