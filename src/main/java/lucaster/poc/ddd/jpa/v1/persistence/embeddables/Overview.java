package lucaster.poc.ddd.jpa.v1.persistence.embeddables;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lucaster.poc.ddd.jpa.v1.persistence.AnemicWeakJpaEntity;

@Entity
public class Overview extends AnemicWeakJpaEntity {

    private Checklist checklist;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @Embedded
    public OverviewSummary getOverviewSummary() {
        Screening screening = checklist.getScreening();
        return new OverviewSummary(screening);
    }

    protected void setOverviewSummary(OverviewSummary overviewSummary) { }
}