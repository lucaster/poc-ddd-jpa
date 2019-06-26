package lucaster.poc.ddd.jpa.v1.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.ExpViewChild;

@Entity
@Table(name = "EXP_VIEW_CHILD")
public class AnemicJpaExpViewChild extends AnemicJpaEntity implements ExpViewChild {

    private String name;

    public AnemicJpaExpViewChild(ExpViewChild child) {
        this(child.getName());
    }

    public AnemicJpaExpViewChild(String name) {
        this.name = name;
    }

    protected AnemicJpaExpViewChild() {
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
}