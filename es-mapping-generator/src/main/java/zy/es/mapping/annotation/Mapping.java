package zy.es.mapping.annotation;

import zy.es.mapping.constant.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    /**
     * 数据类型
     */
    DataType type() default DataType.DEFAULT;

    /**
     * ELasticseaech默认会索引所有的字段，enabled设为false的字段，es会跳过字段内容，该字段只能从_source中获取，但是不可搜。
     */
    boolean enabled() default true;

    /**
     * 无索引
     */
    boolean index() default true;

    /**
     * 分词类型
     */
    String analyzer() default "";

    /**
     * 分词类型
     */
    String searchAnalyzer() default "";

    /**
     * 字段索引长度限制
     */
    int ignoreAbove() default -1;

    /**
     * 忽略不规则数据
     */
    boolean ignoreMalformed() default false;

    /**
     * 是否包含在_all字段里面
     */
    boolean includeInAll() default true;

    /**
     * 单独保存字段
     */
    boolean store() default false;

    /**
     * 词向量包含了文本被解析以后的以下信息：
     * <p>
     * 词项集合
     * 词项位置
     * 词项的起始字符映射到原始文档中的位置。
     */
    String termVector() default "";

    /**
     * 设置字段的权重
     */
    int boost() default -1;

    /**
     * 清除脏数据
     */
    boolean coerce() default false;

    /**
     * 加快排序、聚合操作，在建立倒排索引的时候，额外增加一个列式存储映射
     */
    boolean docValues() default true;

    /**
     * 值为null的字段不索引也不可以搜索，null_value参数可以让值为null的字段显式的可索引、可搜索
     */
    String nullValue() default "";

    /**
     * DATE类型的格式
     */
    String format() default "";
}
