package mapper;

import zy.es.mapping.annotation.Index;
import zy.es.mapping.annotation.Mapping;
import zy.es.mapping.constant.DataType;

@Index(name = "test", shards = 3)
public class TestModel {
    @Mapping(index = false)
    public int[] arr;
    public B[] b;

    public static class B {
        @Mapping(type = DataType.TEXT, index = false)
        public String test = "sdfasdfasd";
    }
}
