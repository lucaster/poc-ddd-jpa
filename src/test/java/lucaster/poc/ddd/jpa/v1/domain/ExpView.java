package lucaster.poc.ddd.jpa.v1.domain;

// TODO: maybe Domain interfaces are redundant and only add complexity..? 
// ...Or they help connecting the Anemic Jpa classes to Domain concemps?
public interface ExpView<T extends ExpViewChild>
        extends ExpViewIndependentFields, ExpViewDerivedFields, HasExpViewChildren<T> {

}
