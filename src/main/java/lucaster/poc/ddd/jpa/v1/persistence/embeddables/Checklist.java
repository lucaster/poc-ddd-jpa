package lucaster.poc.ddd.jpa.v1.persistence.embeddables;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lucaster.poc.ddd.jpa.v1.persistence.AnemicJpaEntity;

@Entity
public class Checklist extends AnemicJpaEntity {

    private Screening screening;
    private Overview overview;

    @OneToOne(mappedBy = "checklist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Screening getScreening() {
        return screening;
    }

    @OneToOne(mappedBy = "checklist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Overview getOverview() {
        return overview;
    }

    @Transient
    public OverviewSummary getOverviewSummary() {
        return new OverviewSummary(screening.getScreeningField1(), screening.getScreeningField2());
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
        screening.setChecklist(this);
    }

    public void setOverview(Overview overview) {
        this.overview = overview;
        overview.setChecklist(this);
    }
}