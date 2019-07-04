package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.Child;
import lucaster.poc.ddd.jpa.v1.domain.Parent;

/**
 * To be used with for read-only use-cases (e.g. reports or old versions of
 * ExpView which derived fields formulas are different from the current
 * requirements)
 */
@Entity
@Table(name = "PARENT")
public class AnemicJpaParent extends AnemicJpaEntity implements Parent {

	private long addend1;
	private long addend2;
	private long addend3;
	private long totalSum;
	private double aggregatedIndex;
	private Set<AnemicJpaChild> children = new HashSet<>();

	public AnemicJpaParent(Parent base) {
		this(base.getAddend1(), base.getAddend2(), base.getAddend3(), base.getTotalSum(), base.getAggregatedIndex());
	}

	public AnemicJpaParent(long addend1, long addend2, long addend3, long totalSum, double aggregatedIndex) {
		this.addend1 = addend1;
		this.addend2 = addend2;
		this.addend3 = addend3;
		this.totalSum = totalSum;
		this.aggregatedIndex = aggregatedIndex;
	}

	protected AnemicJpaParent() {
		this(0, 0, 0, 0, 0);
	}

	@Override
	@Column(name = "ADDEND_1", nullable = false)
	public long getAddend1() {
		return this.addend1;
	}

	@Override
	@Column(name = "ADDEND_2", nullable = false)
	public long getAddend2() {
		return this.addend2;
	}

	@Override
	@Column(name = "ADDEND_3", nullable = false)
	public long getAddend3() {
		return this.addend3;
	}

	@Override
	@Column(name = "TOTAL_SUM", nullable = false)
	public long getTotalSum() {
		return this.totalSum;
	}

	@Override
	@Column(name = "AGGREGATED_INDEX", nullable = false)
	public double getAggregatedIndex() {
		return this.aggregatedIndex;
	}

	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL })
	@JoinTable(
		name ="PARENT_CHILD",
		joinColumns = @JoinColumn(name = "PARENT_ID"),
		inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
	protected Set<AnemicJpaChild> getChildren() {
		return this.children;
	}

	@Override
	public Set<Child> children() {
		return Collections.<Child>unmodifiableSet(this.children);
	}

	@Override
	public void addChildren(Set<Child> children) {
		for (Child child : children) {
			AnemicJpaChild anemicJpaChild = (AnemicJpaChild) child;
			this.children.add(anemicJpaChild);
		}
	}

	@Override
	public void addChildren(Child... children) {
		for (Child child : children) {
			AnemicJpaChild anemicJpaChild = (AnemicJpaChild) child;
			this.children.add(anemicJpaChild);
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

	protected void setAddend1(long addend1) {
		this.addend1 = addend1;
	}

	protected void setAddend2(long addend2) {
		this.addend2 = addend2;
	}

	protected void setAddend3(long addend3) {
		this.addend3 = addend3;
	}

	protected void setTotalSum(long totalSum) {
		this.totalSum = totalSum;
	}

	protected void setAggregatedIndex(double aggregatedIndex) {
		this.aggregatedIndex = aggregatedIndex;
	}

	protected void setChildren(Set<AnemicJpaChild> children) {
		this.children = (children);
	}
}
