package lucaster.poc.ddd.jpa.v1.utils;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public final class JpaUtils {

	@SafeVarargs
	public static <T> T commitInJpa(Function<EntityManager, ? extends Object>... fns) {

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