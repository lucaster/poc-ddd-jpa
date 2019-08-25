package lucaster.poc.statemachine.exploration2;

import java.util.UUID;

import javax.resource.spi.IllegalStateException;

class StateMachineInstance implements ProcessInstance {
	
	private final String id;

	// Enum -> PROCESS_DEFINITION_ID := 'EXAMPLE_SM_PROCESS' enum name as id is ok for process definition
    private final StateMachineProcessDefinition pd;

    // Enum -> STATE_ID := 'STATE1' in data-driven il nome enum non sarà univoco tra tutti gli stati di tutte le sm
    // interface
    private StateMachineState activeState;

    StateMachineInstance(StateMachineProcessDefinition pd) {
    	this(makeId(), pd);
    }

    StateMachineInstance(StateMachineProcessDefinition pd, ExampleSmStates activeState) {
		this(makeId(), pd, activeState);
	}

    StateMachineInstance(String id, StateMachineProcessDefinition pd) {
    	this(id, pd, null);
    }
    
    StateMachineInstance(String id, StateMachineProcessDefinition pd, StateMachineState activeState) {
    	this.pd = pd;
        this.id = id;
        this.activeState = activeState;
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
    
    StateMachineState getActiveState() {
        return activeState;
    }

    // Normally should only be set upon creation, or in AM
    void setActiveState(StateMachineState state) {
        this.activeState = state;
    }

	private static String makeId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void executeTask(Task task) {
		StateMachineTransition transition = (StateMachineTransition) task;
		try {
			executeTransition(transition);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}

	void executeTransition(StateMachineTransition transition) throws IllegalStateException {
		StateMachineState activeState = getActiveState();
		if (!activeState.equals(transition.getFrom())) {
			throw new IllegalStateException();
		}
		setActiveState(transition.getTo());
	}

	@Override
	public boolean isCompleted() {
		return getActiveState().isFinal();
	}
}