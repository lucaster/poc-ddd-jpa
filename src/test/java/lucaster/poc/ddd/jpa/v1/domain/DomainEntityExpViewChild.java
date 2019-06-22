package lucaster.poc.ddd.jpa.v1.domain;

public class DomainEntityExpViewChild extends DomainEntity implements ExpViewChild {

    private String name;

    public DomainEntityExpViewChild(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}