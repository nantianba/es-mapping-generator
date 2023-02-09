package mapper;

import zy.es.mapping.annotation.Index;
import zy.es.mapping.annotation.Mapping;
import zy.es.mapping.constant.DataType;
import zy.es.mapping.constant.DynamicMode;

import java.util.List;

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

    @Mapping(index = false)
    public List<Integer> arr2;
    @Mapping(index = false)
    public Integer integer2;
    @Mapping(type = DataType.KEYWORD)
    public List<String> arr3;
    @Mapping(type = DataType.NESTED)
    public B[] barr;

    @Mapping(type = DataType.NESTED)
    public B b;
    @Mapping()
    public List<B> blist;
    @Mapping(type = DataType.KEYWORD)
    public String keyword;
    @Mapping(type = DataType.DATE, format = "epoch_millis")
    public long time;
    @Mapping(type = DataType.GEO_POINT)
    public String geo;

    public static class B {
        @Mapping(type = DataType.KEYWORD)
        public String test;
    }
}
