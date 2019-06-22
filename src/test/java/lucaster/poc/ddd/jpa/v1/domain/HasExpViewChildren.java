package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Set;

public interface HasExpViewChildren<T extends ExpViewChild> {
    Set<T> children();
    void addAllChildren(Set<T> children);
    void clearChildren();
    void removeChild(T child);
}