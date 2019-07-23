package lucaster.poc.ddd.jpa.orm_xml;

import static lucaster.poc.ddd.jpa.v1.utils.JpaUtils.commitInJpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class OrmXmlTest {

    private static final String TEST_ORM_XML = "testOrmXml";

    @Test
    public void canCreateEm() {
        // Arrange
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(TEST_ORM_XML);

        // Act
        emf.createEntityManager();

        // Assert
    }

    @Test
    public void canPersistDomainEntity() {
        // Arrange
        commitInJpa(TEST_ORM_XML, 
            new Function<EntityManager, Void>() {
                @Override
                public Void apply(EntityManager t) {
                    // Act
                    return null;
                }
            }
        );
        
        // Assert
    }
}