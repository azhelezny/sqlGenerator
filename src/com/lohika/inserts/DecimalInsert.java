package com.lohika.inserts;

/**
 * @author Andrey Zhelezny
 *         Date: 11/24/15
 */
public class DecimalInsert extends AbstractInsert {
    public DecimalInsert(String tableName) {
        super(tableName);
        fill(5);
    }

    public DecimalInsert(String tableName, int precision) {
        super(tableName);
        fill(precision);
    }

    private void fill(int precision) {
        if (precision < 5) {
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
        }
        if (precision >= 5) {
            add("70864");
            add("73904");
            add("65057");
            add("57471");
            add("29974");
            add("72894");
            add("-51623");
            add("16463");
            add("100");
            add("4583");
            add("94715");
            add("300");
            add("97952");
            add("55659");
            add("71250");
            add("30142");
            add("-38566");
            add("76902");
            add("68294");
            add("-8961");
            add("76581");
            add("1623");
            add("35808");
            add("79322");
            add("-100");
            add("25259");
        }
        add("NULL");
        add("0");
    }
}
