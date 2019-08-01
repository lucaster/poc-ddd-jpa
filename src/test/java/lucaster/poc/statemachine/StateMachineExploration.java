package lucaster.poc.statemachine;

import java.io.Serializable;
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
    ProcessInstance findProcessInstance(Serializable domainEntityIdentifier);
}
interface ProcessInstance {
    ProcessDefinition getProcessDefinition();
    State getActiveState();
    Set<Transition> getActiveTransitions();
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
