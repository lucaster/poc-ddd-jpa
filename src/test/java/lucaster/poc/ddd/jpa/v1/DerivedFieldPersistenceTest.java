package lucaster.poc.ddd.jpa.v1;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.Test;

import lucaster.poc.ddd.jpa.v1.domain.DomainParent;
import lucaster.poc.ddd.jpa.v1.persistence.AnemicJpaParent;
import lucaster.poc.ddd.jpa.v1.persistence.JpaDomainParent;
import lucaster.poc.ddd.jpa.v1.utils.JpaUtils;
import lucaster.polyfill.Function;

public class DerivedFieldPersistenceTest {

    UUID parentId;

    @Test
    public void persistAsDomainRetrieveAsAnemic() {
        JpaUtils.commitInJpa(
            new Function<EntityManager, Void>() {

                @Override
                public Void apply(EntityManager em) {
                    // Arrange
                    DomainParent parent = new JpaDomainParent(1000, 100, 10);
                    em.persist(parent);
                    parentId = parent.getId();
                    return null;
                }            
            },
            new Function<EntityManager, Void>() {

                @Override
                public Void apply(EntityManager em) {
                    // Act
                    AnemicJpaParent anemicParent = em.find(AnemicJpaParent.class, parentId);
                    // Assert
                    assertEquals(1110, anemicParent.getTotalSum());
                    return null;
                }
            }
        );
    }
}