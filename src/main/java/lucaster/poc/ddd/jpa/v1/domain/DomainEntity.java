package lucaster.poc.ddd.jpa.v1.domain;

import java.util.UUID;

public abstract class DomainEntity {

	private UUID id = UUID.randomUUID();

	public UUID getId() {
		return this.id;
	}

	protected void setId(UUID id) {
		this.id = id;
	}
}
