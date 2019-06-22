package lucaster.poc.ddd.jpa.v1.domain;

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

	@Override
	public Set<DomainEntityExpViewChild> getChildren() {
		return (children);
	}

	@Override
	public void addAllChildren(Set<DomainEntityExpViewChild> children) {
		this.children.addAll(children);
	}

	@Override
	public void clearChildren() {
		this.children.clear();
	}

	protected void setChildren(Set<? extends DomainEntityExpViewChild> children) {
		@SuppressWarnings("unchecked")
		Set<DomainEntityExpViewChild> cast = (Set<DomainEntityExpViewChild>) children;
		this.children = cast;
	}
}
