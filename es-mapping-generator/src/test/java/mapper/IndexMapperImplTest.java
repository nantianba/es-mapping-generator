package mapper;


import org.junit.jupiter.api.Test;
import zy.es.mapping.IndexDefinition;
import zy.es.mapping.exception.IndexValidateException;
import zy.es.mapping.mapper.IndexMapperImpl;

public class IndexMapperImplTest {

    @Test
    public void mapping() throws IndexValidateException {
        IndexDefinition indexDefinition = new IndexMapperImpl().mapping(TestModel.class);

        System.out.println("indexDefinition.name() = " + indexDefinition.name());
        System.out.println("indexDefinition.mapping() = " + indexDefinition.mapping());
    }
}