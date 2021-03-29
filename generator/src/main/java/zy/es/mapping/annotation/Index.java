package zy.es.mapping.annotation;

import zy.es.mapping.constant.DynamicMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
    String name();

    /**
     * the name of type
     * default name is the same as index name
     */
    String type() default "";

    /**
     * 所有字段是否包含在_all字段里面
     */
    boolean includeInAll() default true;

    DynamicMode dynamic() default DynamicMode.DEFAULT;
    /**
     * 是否要求 指定 routing
     */
    boolean routingRequired() default false;

    /**
     * number of replicas
     */
    int replicas() default 0;

    /**
     * number of shards
     */
    int shards() default 0;


}
