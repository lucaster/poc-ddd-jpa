package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.DomainChild;

@Entity
@Table(name = "EXP_VIEW_CHILD")
public class JpaDomainChild extends DomainChild {

    public JpaDomainChild(DomainChild base) {
        super(base);
    }

    public JpaDomainChild(String name) {
        super(name);
    }

    protected JpaDomainChild() {
        this((String)null);
    }

    @Override
    @Id
    @Column(name = "ID")
    public UUID getId() {
        return super.getId();
    }

    @Override
    @Column(name = "NAME")
    public String getName() {
        return super.getName();
    }

    protected void setId(UUID id) {
    }

    protected void setName(String name) {
    }
}