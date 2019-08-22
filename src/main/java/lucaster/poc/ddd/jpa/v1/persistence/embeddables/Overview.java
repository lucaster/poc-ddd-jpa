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

    public Overview(Checklist checklist) {
        this.checklist = checklist;
    }

    protected Overview() { }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    public Checklist getChecklist() {
        return checklist;
    }

    protected void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @Embedded
    public OverviewSummary getOverviewSummary() {
       return checklist.getOverviewSummary();
    }

    protected void setOverviewSummary(OverviewSummary overviewSummary) { }
}