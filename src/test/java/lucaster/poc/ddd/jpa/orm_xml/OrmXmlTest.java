package lucaster.poc.ddd.jpa.orm_xml;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class OrmXmlTest {

    @Test
    public void canCreateEm() {
        // Arrange
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testOrmXml");
        
        // Act
        EntityManager em = emf.createEntityManager();

        // Assert
    }

    @Test
    public void canPersistDomainEntity() {
        // Arrange
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testOrmXml");
        EntityManager em = emf.createEntityManager();
        
        // Act

        // Assert
    }
}