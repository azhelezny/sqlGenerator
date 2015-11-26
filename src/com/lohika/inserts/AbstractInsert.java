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
            case NUMERIC:
                return new DecimalInsert(tableName);
            case DECIMAL:
                return new DecimalInsert(tableName);
            case DATE:
                return new DateInsert(tableName);
            case TIME:
                return new TimeInsert(tableName);
            case TIMESTAMP:
                return new TimestampInsert(tableName);
            case CHAR:
            case VARCHAR:
            case LONG_VARCHAR:
                return new CharInsert(tableName);
            case CHAR_FOR_BIT_DATA:
            case VARCHAR_FOR_BIT_DATA:
            case LONG_VARCHAR_FOR_BIT_DATA:
                return new CharForBitData(tableName);
            case BOOLEAN:
                return new BooleanInsert(tableName);
            default:
                throw new AssertionError("unable to find insert statement for type " + type.toString());
        }
    }

    public static AbstractInsert getInsert(String tableName, DataType type, int size) {
        switch (type) {
            case CHAR:
            case VARCHAR:
                return new CharInsert(tableName, size);
            case CHAR_FOR_BIT_DATA:
            case VARCHAR_FOR_BIT_DATA:
                return new CharForBitData(tableName, size);
            case FLOAT:
                return new FloatInsert(tableName, size);
            case DECIMAL:
                return new DecimalInsert(tableName, size);
            case NUMERIC:
                return new DecimalInsert(tableName, size);
            default:
                throw new AssertionError("unable to find insert statement for type " + type.toString());
        }
    }
}