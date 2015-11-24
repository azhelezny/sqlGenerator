package com.lohika.inserts;

import com.lohika.types.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Andrey Zhelezny
 *         Date: 11/24/15
 */
public abstract class AbstractInsert {
    private String tableName;
    protected List<String> values = new ArrayList<String>();

    public AbstractInsert(String tableName) {
        this.tableName = tableName;
    }

    protected void add(String value) {
        values.add(value);
    }

    protected final String insertPattern = "INSERT INTO %s (%s) values (%s);";

    protected String getRandomValue() {
        return values.get(new Random().nextInt(values.size()));
    }

    public String getInsertStatement(int columnsNumber) {
        StringBuilder columns = new StringBuilder();
        for (int i = 1; i <= columnsNumber; i++)
            columns.append("column_").append(i).append(", ");
        columns.deleteCharAt(columns.length() - 1);
        columns.deleteCharAt(columns.length() - 1);

        StringBuilder inserts = new StringBuilder();
        for (int i = 1; i <= columnsNumber; i++)
            inserts.append(getRandomValue()).append(", ");
        inserts.deleteCharAt(inserts.length() - 1);
        inserts.deleteCharAt(inserts.length() - 1);

        return String.format(insertPattern, tableName, columns, inserts);
    }

    public String getInsertFromSelect() {
        return String.format("INSERT INTO %s (SELECT * FROM %s);", tableName, tableName);
    }

    public static AbstractInsert getInsert(String tableName, DataType type) {
        switch (type) {
            case SMALLINT:
                return new SmallintInsert(tableName);
            case INTEGER:
                return new IntegerInsert(tableName);
            case BIGINT:
                return new BigintInsert(tableName);
            case REAL:
                return new RealInsert(tableName);
            case DOUBLE:
                return new DoubleInsert(tableName);
            case FLOAT:
                return new FloatInsert(tableName);
            default:
                throw new AssertionError("unable to find insert statement for type " + type.toString());
        }
    }
}