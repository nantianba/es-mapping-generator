package zy.es.mapping.mapper;

import org.apache.commons.lang3.StringUtils;
import zy.es.mapping.IndexDefinition;
import zy.es.mapping.IndexMapper;
import zy.es.mapping.IndexTemplateDefinition;
import zy.es.mapping.annotation.Index;
import zy.es.mapping.annotation.IndexTemplate;
import zy.es.mapping.annotation.Mapping;
import zy.es.mapping.constant.DataType;
import zy.es.mapping.constant.DynamicMode;
import zy.es.mapping.exception.IndexValidateException;
import zy.es.mapping.tools.Json;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class IndexMapperImpl implements IndexMapper {

    @Override
    public IndexDefinition mapping(Class clazz) throws IndexValidateException {
        final Map<String, Object> indexMapping = mapIndex(clazz);
        final String name = getName(clazz);
        final String type = getType(clazz);

        final Map<String, Object> indexSetting = getSetting(clazz);

        return new DefaultIndexDefinition(name, type, Json.toJson(indexMapping), Json.toJson(indexSetting));
    }

    @Override
    public IndexTemplateDefinition template(Class clazz) throws IndexValidateException {
        final IndexDefinition indexDefinition = mapping(clazz);

        final IndexTemplate indexTemplate = (IndexTemplate) clazz.getDeclaredAnnotation(IndexTemplate.class);

        if (indexTemplate == null) {
            throw new IndexValidateException("no index template defined");
        }

        if (StringUtils.isEmpty(indexTemplate.name())) {
            throw new IndexValidateException("no template name");
        }

        if (indexTemplate.indexPatterns().length == 0) {
            throw new IndexValidateException("no index patter");
        }

        for (String indexPattern : indexTemplate.indexPatterns()) {
            if (!StringUtils.endsWith(indexPattern, "*")
                    || StringUtils.length(indexPattern) < 2) {
                throw new IndexValidateException("template name error");
            }
        }

        return new DefaultIndexTemplateDefinition(indexTemplate.name(), indexDefinition.mapping(), indexDefinition.setting(),
                indexTemplate.order(), indexTemplate.indexPatterns(), indexDefinition.type());

    }

    private String getName(Class clazz) {
        Index index = (Index) clazz.getDeclaredAnnotation(Index.class);

        return index.name();
    }

    private String getType(Class clazz) {
        Index index = (Index) clazz.getDeclaredAnnotation(Index.class);

        return StringUtils.defaultIfEmpty(index.type(), index.name());
    }

    private Map<String, Object> getSetting(Class clazz) {
        Index index = (Index) clazz.getDeclaredAnnotation(Index.class);

        TreeMap<String, Object> map = new TreeMap<>();
        if (index.shards() > 0) {
            map.put("number_of_shards", index.shards());
        }
        if (index.replicas() > 0) {
            map.put("number_of_replicas", index.replicas());
        }
        return map;
    }

    private Map<String, Object> mapIndex(Class clazz) throws IndexValidateException {
        TreeMap<String, Object> mapping = new TreeMap<>();

        Field[] fields = clazz.getFields();

        validate(clazz);

        validate(fields);

        addIndexParam(mapping, clazz);

        mapping.put("properties", mapFields(clazz));

        return mapping;
    }

    private void validate(Class clazz) throws IndexValidateException {
        // TODO: 2021/3/29
        Index index = (Index) clazz.getDeclaredAnnotation(Index.class);

        if (index == null) {
            throw new IndexValidateException("index definition is not defined");
        }
        if ("".equals(index.name())) {
            throw new IndexValidateException("index name is empty");
        }
    }

    private void validate(Field[] fields) throws IndexValidateException {
        // TODO: 2021/3/29
        if (fields.length == 0) {
            throw new IndexValidateException("no field defined");
        }
    }

    private void addIndexParam(TreeMap<String, Object> mapping, Class clazz) {
        Index index = (Index) clazz.getDeclaredAnnotation(Index.class);

        if (!index.includeInAll()) {
            mapping.put("include_in_all", false);
        }
        if (index.dynamic() != DynamicMode.DEFAULT) {
            mapping.put("dynamic", index.dynamic().value);
        }

        if (index.routingRequired()) {
            TreeMap<Object, Object> map = new TreeMap<>();

            map.put("required", true);

            mapping.put("routing", map);
        }
    }

    private Map<String, Object> mapFields(Class clazz) {
        TreeMap<String, Object> mapping = new TreeMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            mapping.put(field.getName(), mapField(field));
        }


        return mapping;
    }

    private Map<String, Object> mapField(Field field) {
        TreeMap<String, Object> description = new TreeMap<>();

        Mapping mapping = field.getAnnotation(Mapping.class);

        DataType type = mapType(field, mapping);

        description.put("type", type.value);
        if (type == DataType.OBJECT) {
            description.put("properties", mapFields(field.getType()));
        }

        if (mapping != null) {
            processMappingAnnotation(description, mapping);
        }

        return description;
    }

    private DataType mapType(Field field, Mapping mapping) {
        if (mapping != null
                && mapping.type() != DataType.DEFAULT) {

            return mapping.type();
        }

        Class<?> javaType = field.getType();

        if (javaType.isArray()) {
            if (javaType.getComponentType() == byte.class) {
                return DataType.BINARY;
            }

            javaType = javaType.getComponentType();
        }

        if (javaType.isPrimitive()) {
            return DataType.fromPrimitive(javaType);
        }
        if (javaType.isAssignableFrom(Number.class)) {
            return DataType.fromNumber(javaType);
        }

        if (javaType == String.class) {
            return DataType.TEXT;
        }

        return DataType.OBJECT;
    }

    private void processMappingAnnotation(TreeMap<String, Object> description, Mapping mapping) {
        if (!mapping.enabled()) {
            description.put("enabled", false);
        }
        if (!mapping.index()) {
            description.put("index", false);
        }
        if (StringUtils.isNotBlank(mapping.analyzer())) {
            description.put("analyzer", mapping.analyzer());
        }
        if (StringUtils.isNotBlank(mapping.searchAnalyzer())) {
            description.put("search_analyzer", mapping.searchAnalyzer());
        }
        if (mapping.ignoreAbove() > 0) {
            description.put("ignore_above", mapping.ignoreAbove());
        }
        if (mapping.ignoreMalformed()) {
            description.put("ignore_malformed", true);
        }
        if (!mapping.includeInAll()) {
            description.put("include_in_all", false);
        }
        if (mapping.store()) {
            description.put("store", true);
        }
        if (StringUtils.isNotBlank(mapping.termVector())) {
            description.put("term_vector", mapping.termVector());
        }
        if (mapping.boost() > 0) {
            description.put("boost", mapping.boost());
        }
        if (mapping.coerce()) {
            description.put("coerce", true);
        }
        if (!mapping.docValues()) {
            description.put("doc_values", false);
        }
        if (StringUtils.isNotBlank(mapping.nullValue())) {
            description.put("null_value", mapping.nullValue());
        }
        if (StringUtils.isNotBlank(mapping.format())) {
            description.put("format", mapping.format());
        }

    }

    private static class DefaultIndexDefinition implements IndexDefinition {


        private final String name;
        private final String type;
        private final String mapping;
        private final String setting;

        private DefaultIndexDefinition(String name, String type, String mapping, String setting) {
            this.name = name;
            this.type = type;
            this.mapping = mapping;
            this.setting = setting;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String type() {
            return type;
        }

        @Override
        public String mapping() {
            return mapping;
        }

        @Override
        public String setting() {
            return setting;
        }
    }

    private static final class DefaultIndexTemplateDefinition implements IndexTemplateDefinition {
        private final String mapping;
        private final String setting;
        private final int order;
        private final String[] indexPatterns;
        private final String name;
        private final String type;

        private DefaultIndexTemplateDefinition(String name, String mapping, String setting, int order, String[] indexPatterns, String type) {
            this.mapping = mapping;
            this.setting = setting;
            this.order = order;
            this.indexPatterns = indexPatterns;
            this.name = name;
            this.type = type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String type() {
            return type;
        }

        @Override
        public String mapping() {
            return mapping;
        }

        @Override
        public String setting() {
            return setting;
        }

        @Override
        public int order() {
            return order;
        }

        @Override
        public String[] indexPatterns() {
            return indexPatterns;
        }
    }
}
