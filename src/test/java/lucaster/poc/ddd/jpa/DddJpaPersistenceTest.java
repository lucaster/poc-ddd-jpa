package lucaster.poc.ddd.jpa;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Assert;
import org.junit.Test;

import lucaster.poc.ddd.jpa.v1.domain.DomainEntityExpView;
import lucaster.poc.ddd.jpa.v1.persistence.AnemicJpaExpView;
import lucaster.poc.ddd.jpa.v1.persistence.JpaDomainEntityExpView;

public class DddJpaPersistenceTest {

	private UUID id = null;

	@Test
	public void canPersistDomainEntityAndThenFindIt() {
		commitInJpa(
			new Function<EntityManager, Void>() {
				@Override
				public Void apply(EntityManager em) {
					JpaDomainEntityExpView expView = new JpaDomainEntityExpView(new DomainEntityExpView(1L, 2L, 3L));
					em.persist(expView);
					id = expView.getId();
					return null;
				}
			}, 
			new Function<EntityManager, Void>() {
				@Override
				public Void apply(EntityManager em) {

					DomainEntityExpView expView = em.find(JpaDomainEntityExpView.class, id);
					Assert.assertEquals(id, expView.getId());

					AnemicJpaExpView anemicExpView = em.find(AnemicJpaExpView.class, id);
					Assert.assertEquals(id, anemicExpView.getId());

					assertEquals(expView.getTotalSum(), anemicExpView.getTotalSum());
					assertEquals(expView.getAggregatedIndex(), anemicExpView.getAggregatedIndex(), 0.001d);

					return null;
				}
			}
		);
	}

	@SafeVarargs
	private static <T> T commitInJpa(Function<EntityManager, ? extends Object>... fns) {

		Object result = null;

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();

		for (Function<EntityManager, ? extends Object> fn : fns) {
			transaction.begin();
			try {
				result = fn.apply(em);
				transaction.commit();
			} catch (RuntimeException e) {
				transaction.rollback();
				throw new RuntimeException(e);
			}
		}

		@SuppressWarnings("unchecked")
		T tResult = (T) result;
		return tResult;
	}

}

/*
 * Location: C:\Users\Luca
 * Cavagnoli\eclipse\eclipse-jee-2018-09-win32-x86_64\workspace\Nuova
 * cartella\target\test-classes\!\lucaster\poc\ddd\jpa\DddJpaPersistenceTest.
 * class Java compiler version: 7 (51.0) JD-Core Version: 1.0.5
 */