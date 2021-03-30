package zy.es.mapping.constant;

public enum Consistency {
    ONE("one"),
    ALL("all"),
    QUORUM("quorum"),
    ;

    public final String value;

    Consistency(String value) {
        this.value = value;
    }
}
