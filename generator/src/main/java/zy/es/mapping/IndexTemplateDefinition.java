package zy.es.mapping;

public interface IndexTemplateDefinition {
    String name();

    String type();

    String mapping();

    String setting();

    int order();

    String[] indexPatterns();
}
