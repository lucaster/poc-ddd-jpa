package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@MappedSuperclass
public class AnemicJpaEntity {

    private UUID id;

    @Id
    @Column(name = "ID")
    public UUID getId() {
        return this.id;
    }

    @PrePersist
    protected void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    protected void setId(UUID id) {
        this.id = id;
    }
}