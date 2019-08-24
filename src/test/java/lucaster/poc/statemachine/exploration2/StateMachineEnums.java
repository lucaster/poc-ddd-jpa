package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

enum StateMachineDefinitions implements StateMachineDefinition, ProcessDefinition {

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
    
	@Override public String getProcessDefinitionId() {
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

enum ExampleSmProcessStates implements StateMachineState {
	STATE1("STATE1", false),
	STATE2("STATE2", false),
	STATE3("STATE3", true);
	private final String name;
	private final boolean isFinal;
	ExampleSmProcessStates(String name, boolean isFinal) {
		this.name = name;
		this.isFinal = isFinal;
	}
	@Override public boolean isFinal() {
		return isFinal;
	}
	@Override public String getName() {
		return name;
	}
	// NaturalId, also good for Id
	@Override public String getFullyQualifiedName() {
		return String.format("%s.%s", StateMachineDefinitions.EXAMPLE_SM_PROCESS.getProcessDefinitionId(), getName());
	}
}
enum ExampleSmProcessRoles implements ProcessRole {
    ROLE1,
    ROLE2;
}
enum ExampleSmProcessTransitions implements Task, StateMachineTransition {
    TASK1(	"TASK1",
    		ExampleSmProcessStates.STATE1, 
    		ExampleSmProcessStates.STATE2, 
    		Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE1)
    ), 
    TASK2(	"TASK2",
    		ExampleSmProcessStates.STATE2, 
    		ExampleSmProcessStates.STATE3, 
    		Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE2)
    );
    private final Iterable<ProcessRole> roles;
    private final StateMachineState from;
    private final StateMachineState to;
    private final String name;
    ExampleSmProcessTransitions(String name, StateMachineState from, StateMachineState to, Iterable<ProcessRole> roles) {
    	this.name = name;
    	this.from = from;
    	this.to = to;
        this.roles = roles;
    }
    @Override public String getTaskName() {
        return name;
    }
    @Override public StateMachineState getFrom() {
    	return from;
    }
    @Override public StateMachineState getTo() {
    	return to;
    }
    @Override public Iterable<ProcessRole> getAllowedRoles() {
        return roles;
    }
}