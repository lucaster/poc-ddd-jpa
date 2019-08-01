package lucaster.poc.statemachine;

import java.util.Map;
import java.util.Set;

interface ProcessDefinitionRepository {
    ProcessDefinition findProcessDefinition(String processDefinitionName, String processDefinitionVersion);
}
abstract class ProcessDefinition {
    abstract public String getName();
    abstract public String getVersion();
    abstract protected ProcessInstance newProcessInstance();
}
abstract class ProcessInstanceRepository {
    abstract public ProcessInstance newProcessInstance(ProcessDefinition processDefinition);
    abstract public ProcessInstance findProcessInstance(String domainEntityIdentifier);
}
abstract class ProcessInstance {
    /**
     * E.G. Checlkist
     */
    abstract public String getDomainEntityIdentifier();
    abstract public boolean isTerminated();

    /**
     * @return role -> transitionNames
     */
    abstract public Map<String, Set<String>> getAllowedActivationsStringlyTyped();
    abstract public boolean canActivate(String roleName, String transitionName);    
    abstract public String activate(String roleName, String transitionName) throws IllegalStateException;

    // If method of returned objects are protected, these can become public and respect LoD:
    abstract protected Map<Role, Set<Transition>> getAllowedActivationsTypesafe();
    abstract protected ProcessDefinition getProcessDefinition();
    abstract protected State getActiveState();
    abstract protected Set<Transition> getActiveTransitions();
    abstract protected Transition findTransitionByName(String transitionName);
    abstract protected boolean canActivate(Role role, Transition transition);    
    abstract protected State activate(Role role, Transition transition) throws IllegalStateException;
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
