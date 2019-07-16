package lucaster.poc.ddd.jpa.v1.utils;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public final class JpaUtils {

	private JpaUtils() {}

	@SafeVarargs
	public static <T> T commitInJpa(Function<EntityManager, ? extends Object>... fns) {
		String persistenceUnitName = "test";
		@SuppressWarnings("unchecked")
		T cast = (T) getCommitterInJpa(persistenceUnitName).apply(fns);
		return cast;
	}

	public static <T> Function<Function<EntityManager, ? extends Object>[], T> getCommitterInJpa(final String persistenceUnitName) {
		return new Function<Function<EntityManager, ? extends Object>[], T>() {

			@Override
			public T apply(Function<EntityManager, ? extends Object>[] fns) {
				return commitInJpa(persistenceUnitName, fns);
			}
		};
	}

	@SafeVarargs
	public static <T> T commitInJpa(String persistenceUnitName, Function<EntityManager, ? extends Object>... fns) {
		Object result = null;

		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
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