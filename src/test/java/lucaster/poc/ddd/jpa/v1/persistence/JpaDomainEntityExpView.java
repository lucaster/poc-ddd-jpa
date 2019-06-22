package lucaster.poc.ddd.jpa.v1.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lucaster.poc.ddd.jpa.v1.domain.DomainEntityExpView;
import lucaster.poc.ddd.jpa.v1.domain.ExpViewIndependentFields;

@Entity
@Table(name = "EXP_VIEW")
public class JpaDomainEntityExpView extends DomainEntityExpView {

	public JpaDomainEntityExpView(ExpViewIndependentFields independentFields) {
		super(independentFields);
	}

	public JpaDomainEntityExpView(long addend1, long addend2, long addend3) {
		super(addend1, addend2, addend3);
	}

	protected JpaDomainEntityExpView() {
		this(0L, 0L, 0L);
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
