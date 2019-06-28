package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DomainParent extends DomainEntity implements Parent {

	private long addend1;
	private long addend2;
	private long addend3;
	protected Set<DomainChild> children = new HashSet<>();

	public DomainParent(ParentIndependentFields independentFields) {
		this(independentFields.getAddend1(), independentFields.getAddend2(), independentFields.getAddend3());
	}

	public DomainParent(long addend1, long addend2, long addend3) {
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

	@Override
	public Set<Child> children() {
		return Collections.<Child>unmodifiableSet(children);
	}

	@Override
	public void addAllChildren(Set<Child> children) {
		for (Child child : children) {
			DomainChild domainChild = (DomainChild) child;
			this.children.add(domainChild);
		}
	}

	@Override
	public void clearChildren() {
		this.children.clear();
	}

	@Override
	public void removeChild(Child child) {
		this.children.remove(child);
	}
}
