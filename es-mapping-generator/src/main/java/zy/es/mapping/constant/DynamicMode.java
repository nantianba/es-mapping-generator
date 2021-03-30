package zy.es.mapping.constant;

/**
 * dynamic属性用于检测新发现的字段，有三个取值：
 * <p>
 * true:新发现的字段添加到映射中。（默认）
 * flase:新检测的字段被忽略。必须显式添加新字段。
 * strict:如果检测到新字段，就会引发异常并拒绝文档。
 */
public enum DynamicMode {
    TRUE(true),
    FALSE(false),
    STRICT("strict"),
    DEFAULT(null);
    public final Object value;

    DynamicMode(Object value) {
        this.value = value;
    }
}
