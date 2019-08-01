package lucaster.poc.statemachine;

import java.util.Set;

interface ProcessDefinitionRepository {
    ProcessDefinition findProcessDefinition(String processDefinitionName, String processDefinitionVersion);
}
interface ProcessDefinition {
    ProcessInstance newProcessInstance();
    String getName();
    String getVersion();
}
interface ProcessInstanceRepository {
    ProcessInstance findProcessInstance(String domainEntityIdentifier);
}
interface ProcessInstance {
    ProcessDefinition getProcessDefinition();
    State getActiveState();
    Set<Transition> getActiveTransitions();
    Transition findTransitionByName(String transitionName);
    boolean canActivate(Role role, Transition transition);    
    /**
     * @return the arrival state
     * @throws IllegalStateException if active state is not the transition's from state; 
     *                               if role is not active.
     */
    State activate(Role role, Transition transition) throws IllegalStateException;
    /**
     * E.G. Checlkist
     */
    String getDomainEntityIdentifier();
}
interface State {
    // Unique within ProcessDefinition
    String getName();
}
interface Transition {
    String getName();
    State getFrom();
    State getTo();
    Set<Role> allowedActors();
}
interface Role {
    String getName();
}
