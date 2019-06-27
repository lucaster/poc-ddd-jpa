package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Set;

public interface HasChildren<T extends Child> {
    Set<? extends T> children();
    void addAllChildren(Set<? extends T> children);
    void clearChildren();
    <R extends T> void removeChild(R child);
}