package lucaster.poc.ddd.jpa.v1.domain;

public interface ExpView<T extends ExpViewChild> extends ExpViewIndependentFields, ExpViewDerivedFields, HasExpViewChildren<T> {

}
