package lucaster.poc.ddd.jpa.v1;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;

import lucaster.poc.ddd.jpa.v1.persistence.JpaDomainChild;
import lucaster.poc.ddd.jpa.v1.persistence.JpaDomainParent;
import lucaster.poc.ddd.jpa.v1.utils.JpaUtils;
import lucaster.polyfill.Function;

public class AssociationsTest {

    @Test
    public void persistParentWithChildren() {

        // Arrange
        final JpaDomainParent parent = new JpaDomainParent(1, 1, 1);
        final JpaDomainChild child = new JpaDomainChild("child");
        parent.addChildren(child);

        JpaUtils.commitInJpa(
            // Act
            new Function<EntityManager, Void>() {
                @Override
                public Void apply(EntityManager em) {
                    em.persist(parent);
                    return null;
                }            
            },
            // Assert
            new Function<EntityManager, Void>() {
                @Override
                public Void apply(EntityManager em) {
                    JpaDomainParent foundParent = em.find(parent.getClass(), parent.getId());
                    assertNotNull(foundParent);
                    JpaDomainChild foundChild = em.find(child.getClass(), child.getId());
                    assertNotNull(foundChild);
                    return null;
                }            
            }
        );
    }
}