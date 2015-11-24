package com.lohika.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ammolitor on 6/9/15.
 */

public enum DataType {
    SMALLINT("SMALLINT", "SMALLINT"),
    INTEGER("INTEGER", "INTEGER"),
    BIGINT("BIGINT", "BIGINT"),
    REAL("REAL", "FLOAT"),
    DOUBLE("DOUBLE", "DOUBLE"),
    FLOAT("FLOAT%s", "FLOAT%s"),
    DECIMAL("DECIMAL%s", "DECIMAL%s"),
    NUMERIC("NUMERIC%s", "NUMERIC%s"),
    DATE("DATE", "DATE"),
    TIME("TIME", "TIME"),
    TIMESTAMP("TIMESTAMP", "TIMESTAMP"),
    CHAR("CHAR%s", "CHAR%s"),
    VARCHAR("VARCHAR%s", "TEXT%s"),
    LONG_VARCHAR("LONG VARCHAR", "TEXT"),
    CHAR_FOR_BIT_DATA("CHAR%s FOR BIT DATA", "BLOB%s"),
    VARCHAR_FOR_BIT_DATA("VARCHAR%s FOR BIT DATA", "BLOB%s"),
    LONG_VARCHAR_FOR_BIT_DATA("LONG VARCHAR FOR BIT DATA", "BLOB%s"),
    BOOLEAN("BOOLEAN", "BOOLEAN"),
    BLOB("BLOB%s", "BLOB"),
    CLOB("CLOB%s", "TEXT");

    private String str;
    private String alt;

    public String toFormatString() {
        return this.str;
    }

    public String toAlternativeString() {
        return this.alt;
    }

    DataType(String representation, String alternativeRepresentation) {
        this.str = representation;
        this.alt = alternativeRepresentation;
    }

    public static DataType getRandomDataType() {
        DataType[] types = DataType.values();
        return types[(int) (Math.random() * (types.length))];
        //return DataType.CHAR;
    }

    public static DataType[] getAllTypes(DataType... exclude){
        List<DataType> almostResult = new ArrayList<DataType>(Arrays.asList(DataType.values()));
        for (DataType type : exclude)
            almostResult.remove(type);
        return (DataType[]) almostResult.toArray();
    }

    public static DataType getRandomDataType(DataType... exclude) {
        List<DataType> almostResult = new ArrayList<DataType>(Arrays.asList(DataType.values()));
        for (DataType type : exclude)
            almostResult.remove(type);
        return almostResult.get((int) (Math.random() * (almostResult.size())));
    }
}
// TINYINT, // ERROR 42X94: TYPE TINYINT does not exist.
// NATIONAL_CHAR, // ERROR 0A000: Feature not implemented: NATIONAL CHAR.
// NATIONAL_CHAR_VARYING, // ERROR 0A000: Feature not implemented: NATIONAL CHAR VARYING.
// LONG_NVARCHAR, // ERROR 0A000: Feature not implemented: LONG NVARCHAR.
// NCLOB("NCLOB"), // ERROR 0A000: Feature not implemented: NCLOB.
// BINARY("BINARY"),
// VARBINARY("VARBINARY"), // ERROR 42X94: TYPE VARBINARY does not exist.
// LONGVARBINARY("LONGVARBINARY") // ERROR 42X94: TYPE LONGVARBINARY does not exist.

