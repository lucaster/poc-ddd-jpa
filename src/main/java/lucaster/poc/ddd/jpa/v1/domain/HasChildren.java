package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Set;

public interface HasChildren {
    Set<? extends Child> children();
    void addAllChildren(Set<? extends Child> children);
    void clearChildren();
    void removeChild(Child child);
}