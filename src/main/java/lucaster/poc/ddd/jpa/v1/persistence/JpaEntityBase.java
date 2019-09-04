package lucaster.poc.ddd.jpa.v1.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

// https://vladmihalcea.com/how-to-audit-entity-modifications-using-the-jpa-entitylisteners-embedded-and-embeddable-annotations/

interface Auditable {
    Audit getAudit();
    void setAudit(Audit audit);
}

@Embeddable
class Audit {
    /* @PrePersist */ private Date   createdOn;
    /* @PrePersist */ private String createdBy;
    /* @PreUpdate */  private Date   updatedOn; 
    /* @PreUpdate */  private String updatedBy; 
}

@EntityListeners({ AuditListener.class })
abstract class JpaEntityBase<T extends Serializable> implements Auditable {
    @Id private T id; /* + get set */
    @Version private Integer version; /* + get set */
    private Audit audit;
    @Override @Embedded public Audit getAudit() { return audit; }
    @Override public void setAudit(Audit audit) { this.audit = audit; }
}

// @Dependent should not be needed
class AuditListener  {
    /* 
      @Inject something (does CDI work here? Should work since JPA 2.1) 
      Otherwise: https://stackoverflow.com/questions/10765508/cdi-injection-in-entitylisteners
    */
    @PrePersist public void prePersist(Auditable auditable) { /* work */ }
    @PreUpdate public void preUpdate(Auditable auditable) { /* work */ }
}