package lucaster.poc.ddd.jpa.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

import lucaster.poc.ddd.jpa.v1.persistence.embeddables.Checklist;
import lucaster.poc.ddd.jpa.v1.persistence.embeddables.Overview;
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
                    sc.setChecklist(cl);
                    cl.setScreening(sc);

                    // Senza questo, non c'è nemmeno la Overview in primis
                    Overview ov = new Overview();
                    ov.setChecklist(cl);
                    cl.setOverview(ov);

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

                    String sqlString = "select SCREENINGDERIVEDFIELD from OVERVIEW where id = :id";
                    Query nativeQuery = em.createNativeQuery(sqlString).setParameter("id", clId);
                    Integer screeningDerivedField = (Integer) nativeQuery.getSingleResult();

                    assertEquals((Integer) 3, screeningDerivedField);

                    return null;
                }
            }
        );
    }
}