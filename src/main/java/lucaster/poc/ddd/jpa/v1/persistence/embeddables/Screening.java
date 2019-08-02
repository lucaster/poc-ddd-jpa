package lucaster.poc.ddd.jpa.v1.persistence.embeddables;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lucaster.poc.ddd.jpa.v1.persistence.AnemicWeakJpaEntity;

@Entity
public class Screening extends AnemicWeakJpaEntity {

    private Integer screeningField1;
    private Integer screeningField2;

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

    public Integer getScreeningField1() {
        return screeningField1;
    }

    public void setScreeningField1(Integer screeningField1) {
        this.screeningField1 = screeningField1;
    }

    public Integer getScreeningField2() {
        return screeningField2;
    }

    public void setScreeningField2(Integer screeningField2) {
        this.screeningField2 = screeningField2;
    }
}