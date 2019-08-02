package lucaster.poc.ddd.jpa.v1.persistence.embeddables;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

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

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public void setOverview(Overview overview) {
        this.overview = overview;
    }
}