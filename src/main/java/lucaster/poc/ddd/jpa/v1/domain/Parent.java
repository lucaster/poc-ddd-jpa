package lucaster.poc.ddd.jpa.v1.domain;

// TODO: maybe Domain interfaces are redundant and only add complexity..? 
// ...Or they help connecting the Anemic Jpa classes to Domain concemps?
public abstract class Parent extends DomainEntity implements ParentIndependentFields, ParentDerivedFields, HasChildren {

}
