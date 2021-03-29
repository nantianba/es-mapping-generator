package zy.es.mapping;

import zy.es.mapping.exception.IndexValidateException;

public interface IndexMapper {
    IndexDefinition mapping(Class clazz) throws IndexValidateException;

    IndexTemplateDefinition template(Class clazz) throws IndexValidateException;
}
