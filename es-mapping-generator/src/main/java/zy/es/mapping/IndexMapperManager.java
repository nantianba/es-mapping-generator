package zy.es.mapping;

import zy.es.mapping.mapper.IndexMapperImpl;

public class IndexMapperManager {
    public static IndexMapper getInstance() {
        return new IndexMapperImpl();
    }
}
