package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Set;

public interface HasChildren {
    Set<Child> children();
    void addAllChildren(Set<Child> children);
    void clearChildren();
    void removeChild(Child child);
}