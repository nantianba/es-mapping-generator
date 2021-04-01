# ElasticSearch Mapping Generator 

- This tool allow to generate the mapping or setting for indice when creating them.
- Use annotation to describe the mapping field defined in [ES official docoment](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html)

## Installation
Binaries are deployed on Maven Central, you can import the artifacts to your project:

```xml
<dependency>
    <groupId>com.github.nantianba</groupId>
    <artifactId>es-mapping-generator</artifactId>
    <version>0.1</version>
</dependency>
```
## Core Api
- IndexMapper & IndexDefinition

`IndexDefinition` is the description of index type, told es the name,type,mapping and setting of index 

`IndexMapper` will parse the class we provide and return the `IndexDefinition`.

We can get a default `IndexMapper` using `IndexMapperManager#getInstance`
- Annotations

Class annotationed with `@Index` can be parse by `IndexMapper`,and its fields can be annotationed `@Mapping`

## Example
- Define a data model
```java
@Index(
        name = "test"
        , type = "test_type"
        , shards = 3
        , replicas = 1
        , dynamic = DynamicMode.STRICT
        , routingRequired = true
)
public class TestModel {
    @Mapping(index = false)
    public int[] arr;
    public B[] b;
    @Mapping(type = DataType.KEYWORD)
    public String keyword;
    @Mapping(type = DataType.DATE, format = "epoch_millis")
    public long time;
    @Mapping(type = DataType.GEO_POINT)
    public String geo;

    public static class B {
        @Mapping(type = DataType.TEXT, index = false)
        public String test;
    }
}
```
- Generate the definition of index
```java
IndexDefinition indexDefinition = IndexMapperManager.getInstance().mapping(TestModel.class);

System.out.println("indexDefinition.name() = " + indexDefinition.name());
System.out.println("indexDefinition.type() = " + indexDefinition.type());
System.out.println("indexDefinition.mapping() = " + indexDefinition.mapping());
System.out.println("indexDefinition.setting() = " + indexDefinition.setting());
```
Get the result 
```json
indexDefinition.name() = test
indexDefinition.type() = test_type
indexDefinition.mapping() = {
  "_routing": {
    "required": true
  },
  "dynamic": "strict",
  "properties": {
    "arr": {
      "index": false,
      "type": "integer"
    },
    "b": {
      "properties": {},
      "type": "object"
    },
    "geo": {
      "type": "geo_point"
    },
    "keyword": {
      "type": "keyword"
    },
    "time": {
      "format": "epoch_millis",
      "type": "date"
    }
  }
}
indexDefinition.setting() = {
  "number_of_replicas": 1,
  "number_of_shards": 3
}

```
