package lucaster.poc.statemachine.exploration2;

import java.util.UUID;

class StateMachineInstance implements ProcessInstance {
	
	private final String id = UUID.randomUUID().toString();

	// Enum -> PROCESS_DEFINITION_ID := 'EXAMPLE_SM_PROCESS' enum name as id is ok for process definition
    private final StateMachineDefinitions pd;

    // Enum -> STATE_ID := 'STATE1' in data-driven il nome enum non sarà univoco tra tutti gli stati di tutte le sm
    // interface
    private StateMachineState activeState;

    StateMachineInstance(StateMachineDefinitions pd) {
        this.pd = pd;
    }
    
    @Override public String getProcessInstanceId() {
    	return id;
    }

    @Override public ProcessDefinition getProcessDefinition() {
        return pd;
    }

    @Override public Iterable<Task> getActiveTasks() {
        StateMachineState activeState = getActiveState();
        Iterable<Task> activeTasks = pd.getOutgoingTransitions(activeState);
        return activeTasks;
    }

    String getFullyQualifiedActiveStateName() {
    	return activeState.getFullyQualifiedName();
    }
    
    private StateMachineState getActiveState() {
        return activeState;
    }

    // Normally should only be set upon creation, or in AM
    void setActiveState(StateMachineState state) {
        this.activeState = state;
    }
}