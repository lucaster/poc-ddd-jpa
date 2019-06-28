package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Set;

public interface HasChildren {
    Set<Child> children();
    void addChildren(Set<Child> children);
    void addChildren(Child ...children);
    void clearChildren();
    void removeChild(Child child);
}