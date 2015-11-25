package com.lohika.inserts;

/**
 * @author Andrey Zhelezny
 *         Date: 11/24/15
 */
public class BooleanInsert extends AbstractInsert {
    public BooleanInsert(String tableName) {
        super(tableName);
        add("false");
        add("true");
        add("NULL");
    }
}
