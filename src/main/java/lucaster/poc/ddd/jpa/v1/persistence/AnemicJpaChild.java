package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.Child;

@Entity
@Table(name = "CHILD")
public class AnemicJpaChild extends Child {

    private String name;

    public AnemicJpaChild(Child child) {
        this(child.getName());
    }

    public AnemicJpaChild(String name) {
        this.name = name;
    }

    protected AnemicJpaChild() {
        this((String)null);
    }

    @Override
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    @Id
    @Column(name = "ID")
    public UUID getId() {
        return super.getId();
    }

    @PrePersist
    protected void prePersist() {
        if (getId() == null) {
            setId(UUID.randomUUID());
        }
    }

    @Override
    protected void setId(UUID id) {
        super.setId(id);
    }
}