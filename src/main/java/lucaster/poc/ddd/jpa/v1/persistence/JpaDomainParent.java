package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.DomainChild;
import lucaster.poc.ddd.jpa.v1.domain.DomainParent;
import lucaster.poc.ddd.jpa.v1.domain.ParentIndependentFields;

@Entity
@Table(name = "PARENT")
public class JpaDomainParent extends DomainParent {

	/**
	 * This type of constructor is useful when persisting a new Domain Entity
	 * @param independentFields what's relevant about the Domain Entity instance
	 */
	public JpaDomainParent(ParentIndependentFields independentFields) {
		super(independentFields);
	}

	public JpaDomainParent(long addend1, long addend2, long addend3) {
		super(addend1, addend2, addend3);
	}

	protected JpaDomainParent() {
		this(0, 0, 0);
	}

	@Override
	@Id
	@Column(name = "ID")
	public UUID getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ADDEND_1", nullable = false)
	public long getAddend1() {
		return super.getAddend1();
	}

	@Override
	@Column(name = "ADDEND_2", nullable = false)
	public long getAddend2() {
		return super.getAddend2();
	}

	@Override
	@Column(name = "ADDEND_3", nullable = false)
	public long getAddend3() {
		return super.getAddend3();
	}

	@Override
	@Column(name = "TOTAL_SUM", nullable = false)
	public long getTotalSum() {
		return super.getTotalSum();
	}

	@Override
	@Column(name = "AGGREGATED_INDEX", nullable = false)
	public double getAggregatedIndex() {
		return super.getAggregatedIndex();
	}

	/**
	 * Hibernate wants you to return your reference, otherwise: <br />
	 * <p><i>A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance</i></p>
	 * The fix would be to use field annotations, but we cannot do that because we are inheriting from the Domain Entity.
	 * @deprecated for frameworks only
	 */
	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL }, targetEntity = JpaDomainChild.class)
	@JoinTable(
		name = "PARENT_CHILD",
		joinColumns = @JoinColumn(name = "PARENT_ID"),
		inverseJoinColumns = @JoinColumn(name = "CHILD_ID")
	)
	@Deprecated
	protected Set<DomainChild> getChildren() {
		return this.children;
	}

	/**
	 * HIbernate wants to set your reference, otherwise: <br />
	 * <p><i>A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance</i></p>
	 * The fix would be to use field annotations, but we cannot do that because we are inheriting from the Domain Entity.
	 * @deprecated for frameworks only
	 */
	@Deprecated
	protected void setChildren(Set<DomainChild> children) {
		this.children = children;
	}

	protected void setId(UUID id) {
	}

	protected void setAddend1(long addend1) {
	}

	protected void setAddend2(long addend2) {
	}

	protected void setAddend3(long addend3) {
	}

	protected void setTotalSum(long totalSum) {
	}

	protected void setAggregatedIndex(double aggregatedIndex) {
	}
}
