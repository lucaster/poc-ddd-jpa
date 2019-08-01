package lucaster.poc.statemachine;

import java.util.Map;
import java.util.Set;

abstract class ProcessDefinition {
    abstract public String getName();
    abstract public String getVersion();
}
interface ProcessDefinitionRepository {
    ProcessDefinition findProcessDefinition(String processDefinitionName, String processDefinitionVersion);
}
abstract class ProcessInstance {
    /**
     * Identifier of the Domain Entity this Process Instance manages the workflow of.
     */
    abstract public String getDomainEntityIdentifier();
    abstract public boolean isTerminated();

    /**
     * @return role -> transitionNames
     */
    abstract public Map<String, Set<String>> getAllowedActivationsStringlyTyped();
    abstract public boolean canActivate(String roleName, String transitionName);    
    abstract public String activate(String roleName, String transitionName) throws IllegalStateException;

    // If returned objects are immutable and do not modify anything, these type-safe methods can become public and compromise with LoD:
    abstract protected Map<Role, Set<Transition>> getAllowedActivationsTypesafe();
    abstract protected boolean canActivate(Role role, Transition transition);    
    abstract protected State activate(Role role, Transition transition) throws IllegalStateException;

    abstract protected ProcessDefinition getProcessDefinition();
    abstract protected State getActiveState();
    abstract protected Set<Transition> getActiveTransitions();
    abstract protected Transition findTransitionByName(String transitionName);

    abstract protected Set<Transition> getTransitions();
    abstract protected Set<State> getStates();
    abstract protected Set<Role> getRoles();
}
interface ProcessInstanceFactory {
    ProcessInstance newProcessInstance(ProcessDefinition processDefinition);
}
interface ProcessInstanceRepository {
    ProcessInstance findProcessInstance(String domainEntityIdentifier);
}
abstract class State {
    // Unique within ProcessDefinition
    abstract String getName();
}
abstract class Transition {
    abstract String getName();
    abstract State getFrom();
    abstract State getTo();
    /**
     * @return an immutable set
     */
    abstract Set<Role> allowedActors();
}
abstract class Role {
    abstract String getName();
}
