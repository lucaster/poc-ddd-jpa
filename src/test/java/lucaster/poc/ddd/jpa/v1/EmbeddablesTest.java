package lucaster.poc.ddd.jpa.v1;

import java.util.function.Function;

import javax.persistence.EntityManager;

import org.junit.Test;

import lucaster.poc.ddd.jpa.v1.utils.JpaUtils;


public class EmbeddablesTest {

    @Test
    public void asd() {
        JpaUtils.commitInJpa("embeddables", new Function<EntityManager, Void>() {

            @Override
            public Void apply(EntityManager t) {
                return null;
            }

        }); 
    }
}