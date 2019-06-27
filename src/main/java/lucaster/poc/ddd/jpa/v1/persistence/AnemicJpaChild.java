package lucaster.poc.ddd.jpa.v1.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.Child;

@Entity
@Table(name = "EXP_VIEW_CHILD")
public class AnemicJpaChild extends AnemicJpaEntity implements Child {

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
}