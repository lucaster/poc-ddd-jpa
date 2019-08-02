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

    public void addOverview() {
        if (this.overview == null) {
            overview(new Overview(this));
        }
    }

    @Transient
    public OverviewSummary getOverviewSummary() {
        if (screening != null) {
            return new OverviewSummary(screening.getScreeningField1(), screening.getScreeningField2());
        }
        else {
            return null;
        }
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
        screening.setChecklist(this);
    }

    @Deprecated
    protected void setOverview(Overview overview) {
        overview(overview);
    }

    private void overview(Overview overview) {
        this.overview = overview;
        overview.setChecklist(this);
    }
}