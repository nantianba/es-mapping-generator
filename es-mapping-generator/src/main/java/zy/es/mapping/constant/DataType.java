package zy.es.mapping.constant;

import zy.es.mapping.exception.DataTypeException;

public enum DataType {
    /**
     * 5.X 之前可用
     */
    STRING("string"),
    TEXT("text"),
    KEYWORD("keyword"),

    LONG("long"),
    INTEGER("integer"),
    SHORT("short"),
    BYTE("byte"),
    DOUBLE("double"),
    FLOAT("float"),
    HALF_FLOAT("half_float"),
    SCALED_FLOAT("scaled_float"),

    BOOLEAN("boolean"),
    BINARY("binary"),
    DATE("date"),

    IP("ip"),
    GEO_POINT("geo_point"),
    GEO_SHAPE("geo_shape"),

    INTEGER_RANGE("integer_range"),
    FLOAT_RANGE("float_range"),
    LONG_RANGE("long_range"),
    DOUBLE_RANGE("double_range"),
    DATE_RANGE("date_range"),

    NESTED("nested"),

    OBJECT("object"),
    ARRAY("array"),

    /**
     * 依照字段类型
     */
    DEFAULT(""),
    ;

    public final String value;

    DataType(String value) {
        this.value = value;
    }

    public static DataType fromPrimitive(Class<?> javaType) {
        switch (javaType.getSimpleName().toLowerCase()) {
            case "int":
                return INTEGER;
            case "long":
                return LONG;
            case "byte":
                return BYTE;
            case "short":
                return SHORT;
            case "char":
                return SHORT;
            case "boolean":
                return BOOLEAN;
            case "float":
                return FLOAT;
            case "double":
                return DOUBLE;
            default:
                throw new DataTypeException("no such dataType for:" + javaType);
        }
    }

    public static DataType fromNumber(Class<?> javaType) {
        switch (javaType.getSimpleName().toLowerCase()) {
            case "integer":
                return INTEGER;
            case "long":
                return LONG;
            case "byte":
                return BYTE;
            case "short":
                return SHORT;
            case "character":
                return SHORT;
            case "boolean":
                return BOOLEAN;
            case "float":
                return FLOAT;
            case "double":
                return DOUBLE;
            case "bigdecimal":
                return KEYWORD;
            case "biginteger":
                return KEYWORD;
            default:
                throw new DataTypeException("no such dataType for:" + javaType);
        }
    }
}
