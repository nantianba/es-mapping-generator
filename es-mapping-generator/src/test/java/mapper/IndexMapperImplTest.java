package mapper;


import org.junit.jupiter.api.Test;
import zy.es.mapping.IndexDefinition;
import zy.es.mapping.IndexMapperManager;
import zy.es.mapping.exception.IndexValidateException;

public class IndexMapperImplTest {

    @Test
    public void mapping() throws IndexValidateException {
        IndexDefinition indexDefinition = IndexMapperManager.getInstance().mapping(TestModel.class);

        System.out.println("indexDefinition.name() = " + indexDefinition.name());
        System.out.println("indexDefinition.type() = " + indexDefinition.type());
        System.out.println("indexDefinition.mapping() = " + indexDefinition.mapping());
        System.out.println("indexDefinition.setting() = " + indexDefinition.setting());
    }
}