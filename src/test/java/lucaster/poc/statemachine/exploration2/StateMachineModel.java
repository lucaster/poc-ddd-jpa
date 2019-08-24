package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

enum StateMachineDefinitions implements ProcessDefinition, StateMachineDefinition {

    EXAMPLE_SM_PROCESS("EXAMPLE_SM_PROCESS", Utils.<ExampleSmProcessTransitions>toSet(
    	ExampleSmProcessTransitions.TASK1, 
    	ExampleSmProcessTransitions.TASK2
    ));

	private final String processId;
    private final Iterable<ExampleSmProcessTransitions> tasks;

    StateMachineDefinitions(String processId, Iterable<ExampleSmProcessTransitions> tasks) {
    	this.processId = processId;
        this.tasks = tasks;
    }
    
	@Override public String getProcessId() {
        return processId;
    }

    @Override public Iterable<Task> getOutgoingTransitions(StateMachineState state) {
    	Set<Task> set = new HashSet<>();
    	for (ExampleSmProcessTransitions t : tasks) {
    		if (state.equals(t.getFrom())) {
    			set.add(t);
    		}
    	}
    	return Collections.unmodifiableSet(set);
	}

	@Override public Set<StateMachineState> getStates() {
		Set<StateMachineState> set = new HashSet<>();
    	for (StateMachineTransition t : tasks) {
    		set.add(t.getFrom());
    		set.add(t.getTo());
    	}
    	return Collections.unmodifiableSet(set);
	}

    @Override public Task findTaskByName(String taskName) {
        for (Task t : tasks) {
            if (t.getTaskName().equals(taskName)) {
                return t;
            }
        }
        return null;
    }
}

class StateMachineInstance implements ProcessInstance {

    private final StateMachineDefinitions pd;
    private StateMachineState activeState;

    StateMachineInstance(StateMachineDefinitions pd) {
        this.pd = pd;
    }

    @Override public ProcessDefinition getProcessDefinition() {
        return pd;
    }

    @Override public Iterable<Task> getActiveTasks() {
        StateMachineState activeState = getActiveState();
        Iterable<Task> activeTasks = pd.getOutgoingTransitions(activeState);
        return activeTasks;
    }

    private StateMachineState getActiveState() {
        return activeState;
    }

    // Normally should only be set upon creation, or in AM
    void setActiveState(StateMachineState state) {
        this.activeState = state;
    }
}