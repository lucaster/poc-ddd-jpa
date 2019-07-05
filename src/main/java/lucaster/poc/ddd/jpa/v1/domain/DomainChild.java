package lucaster.poc.ddd.jpa.v1.domain;

public class DomainChild extends Child {

    private String name;

    public DomainChild(Child base) {
        this(base.getName());
	}

    public DomainChild(String name) {
        this.name = name;
    }

	@Override
    public String getName() {
        return name;
    }
}