package lucaster.poc.ddd.jpa.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.UUID;
import java.util.function.Function;

import javax.persistence.EntityManager;

import org.junit.Test;

import lucaster.poc.ddd.jpa.v1.persistence.embeddables.Checklist;
import lucaster.poc.ddd.jpa.v1.persistence.embeddables.Screening;
import lucaster.poc.ddd.jpa.v1.utils.JpaUtils;

public class EmbeddablesTest {

    @Test
    public void mappingIsCorrect() {
        JpaUtils.commitInJpa("embeddables", new Function<EntityManager, Void>() {

            @Override
            public Void apply(EntityManager t) {
                return null;
            }
        });
    }

    private UUID clId;

    @Test
    public void overviewSummaryUpdateUponSourceDataChange() {

        JpaUtils.commitInJpa(
            "embeddables", 
            new Function<EntityManager, Void>() {

                @Override
                public Void apply(EntityManager em) {

                    Checklist cl = new Checklist();
                    Screening sc = new Screening();
                    sc.setScreeningField1(1);
                    sc.setScreeningField2(2);
                    cl.setScreening(sc);

                    // Senza questo, non c'è nemmeno la Overview in primis e quindi no OverviewSummary
                    // Con questo, quando JPA fa il dirty check, crea automaticamente l'OverviewSummary
                    cl.addOverview();

                    em.persist(cl);
                    em.flush();

                    clId = cl.getId();

                    assertNotNull(cl.getId());

                    return null;
                }

            }, 
            new Function<EntityManager, Void>() {

                @Override
                public Void apply(EntityManager em) {

                    
                    BigInteger overviewCount = (BigInteger) em
                                                .createNativeQuery("select count(*) from OVERVIEW where id = :id")
                                                .setParameter("id", clId)
                                                .getSingleResult();
                    
                    assertEquals(BigInteger.ONE, overviewCount);

                    Integer screeningDerivedField = (Integer) em
                                                    .createNativeQuery("select SCREENINGDERIVEDFIELD from OVERVIEW where id = :id")
                                                    .setParameter("id", clId)
                                                    .getSingleResult();

                    assertEquals((Integer) 3, screeningDerivedField);

                    return null;
                }
            }
        );
    }

    @Test
    public void noScreeningNoOverviewSummary() {

        JpaUtils.commitInJpa(
            "embeddables", 
            new Function<EntityManager, Void>() {

                @Override
                public Void apply(EntityManager em) {

                    Checklist cl = new Checklist();

                    // Senza questo, non c'è nemmeno la Overview in primis e quindi no OverviewSummary
                    // Con questo, quando JPA fa il dirty check, crea automaticamente l'OverviewSummary
                    cl.addOverview();

                    em.persist(cl);
                    em.flush();

                    clId = cl.getId();

                    assertNotNull(cl.getId());

                    return null;
                }
            }, 
            new Function<EntityManager, Void>() {

                @Override
                public Void apply(EntityManager em) {

                    BigInteger overviewCount = (BigInteger) em
                                                .createNativeQuery("select count(*) from OVERVIEW where id = :id")
                                                .setParameter("id", clId)
                                                .getSingleResult();
                    
                    assertEquals(BigInteger.ONE, overviewCount);

                    Integer screeningDerivedField = (Integer) em
                                                    .createNativeQuery("select SCREENINGDERIVEDFIELD from OVERVIEW where id = :id")
                                                    .setParameter("id", clId)
                                                    .getSingleResult();

                    assertNull(screeningDerivedField);

                    return null;
                }
            }
        );
    }
}
