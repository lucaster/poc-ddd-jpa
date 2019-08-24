package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AnemicWeakJpaEntity {

    private UUID id;

    @Id
    @Column(name = "ID")
    public UUID getId() {
        return this.id;
    }

    protected void setId(UUID id) {
        this.id = id;
    }
}