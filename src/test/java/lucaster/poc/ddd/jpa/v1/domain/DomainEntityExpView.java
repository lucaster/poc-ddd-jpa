package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DomainEntityExpView extends DomainEntity implements ExpView<DomainEntityExpViewChild> {

	private long addend1;
	private long addend2;
	private long addend3;
	private Set<DomainEntityExpViewChild> children = new HashSet<>();

	public DomainEntityExpView(ExpViewIndependentFields independentFields) {
		this(independentFields.getAddend1(), independentFields.getAddend2(), independentFields.getAddend3());
	}

	public DomainEntityExpView(long addend1, long addend2, long addend3) {
		this.addend1 = addend1;
		this.addend2 = addend2;
		this.addend3 = addend3;
	}

	@Override
	public long getAddend1() {
		return this.addend1;
	}

	@Override
	public long getAddend2() {
		return this.addend2;
	}

	@Override
	public long getAddend3() {
		return this.addend3;
	}

	@Override
	public long getTotalSum() {
		return this.addend1 + this.addend2 + this.addend3;
	}

	@Override
	public double getAggregatedIndex() {
		return (this.addend1 + this.addend2) / this.addend3;
	}

	// Hibernate vuole per forza che gli passi il reference che hai:
	@Override
	public Set<DomainEntityExpViewChild> children() {
		return Collections.unmodifiableSet(children);
	}

	@Override
	public void addAllChildren(Set<DomainEntityExpViewChild> children) {
		this.children.addAll(children);
	}

	@Override
	public void clearChildren() {
		this.children.clear();
	}

	@Override
	public void removeChild(DomainEntityExpViewChild child) {
		this.children.remove(child);
	}

	// Hibernate wants you to return your reference, otherwise:
	// A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance
	protected Set<DomainEntityExpViewChild> getChildren() {
		return children;
	}

	// HIbernate wants to set your reference, otherwise:
	// A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance
	protected void setChildren(Set<DomainEntityExpViewChild> children) {
		this.children = children;
	}
}
