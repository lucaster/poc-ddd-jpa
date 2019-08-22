package lucaster.poc.ddd.jpa.v1.persistence.embeddables;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OverviewSummary {

    private Integer screeningField1;
    private Integer screeningField2;

    public OverviewSummary(Screening screening) {
        if (screening != null) {
            this.screeningField1 = screening.getScreeningField1();
            this.screeningField2 = screening.getScreeningField2();    
        }
    }

    protected OverviewSummary(Integer screeningField1, Integer screeningField2) {
        this.screeningField1 = screeningField1;
        this.screeningField2 = screeningField2;
    }

    protected OverviewSummary() {
        this(null, null);
    }

    @Column
	public Integer getScreeningField1() {
        return screeningField1;
    }

    @Column
    public Integer getScreeningField2() {
        return screeningField2;
    }

    @Column
    public Integer getScreeningDerivedField() {
        return coalesce(getScreeningField1()) + coalesce(getScreeningField2());
    }

    protected void setScreeningField1(Integer screeningField1) { }
    protected void setScreeningField2(Integer screeningField2) { }
    protected void setScreeningDerivedField(Integer i) { }

    private static Integer coalesce(Integer i) {
        if (i == null) {
            return 0;
        } else {
            return i;
        }
    }
}
