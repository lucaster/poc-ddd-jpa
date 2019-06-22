package lucaster.poc.ddd.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class BasicJpaTest {
	@Test
	public void canGetPersistence() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		emf.createEntityManager();
	}
}
