package lucaster.poc.ddd.jpa.v1.domain;

import java.util.Set;

public interface HasExpViewChildren<T extends ExpViewChild> {
    Set<T> getChildren();
    void addAllChildren(Set<T> children);
    void clearChildren();
}