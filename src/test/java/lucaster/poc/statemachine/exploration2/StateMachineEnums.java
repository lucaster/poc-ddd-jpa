package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum values will depend on project
 */
enum StateMachineProcessDefinition implements StateMachineDefinition, ProcessDefinition {

    EXAMPLE_SM_PROCESS(Utils.toSet(
    	ExampleSmTransitions.TASK1, 
    	ExampleSmTransitions.TASK2
    ));

    private final Iterable<ExampleSmTransitions> tasks;

    StateMachineProcessDefinition(Iterable<ExampleSmTransitions> tasks) {
    	// TODO: check only one initial state
    	// TODO: check final states must not have outgoing transitions
    	// TODO: check only final states can have no outgoing transitions
    	// TODO: check connected graph (only initial state can have no incoming transitions)
        this.tasks = tasks;
    }
    
	@Override public String getProcessDefinitionId() {
        return name();
    }

    @Override public Iterable<Task> getOutgoingTransitions(StateMachineState state) {
    	Set<Task> set = new HashSet<>();
    	for (ExampleSmTransitions t : tasks) {
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
    
    @Override public ProcessInstance createProcessInstance() {
    	return new StateMachineInstance(this, ExampleSmStates.STATE1);
    }
}
enum ExampleSmStates implements StateMachineState {
	STATE1("STATE1", true, false),
	STATE2("STATE2", false, false),
	STATE3("STATE3", false, true);
	private final String name;
	private final boolean isInitial;
	private final boolean isFinal;
	ExampleSmStates(String name, boolean isInitial, boolean isFinal) {
		this.name = name;
		this.isInitial = isInitial;
		this.isFinal = isFinal;
	}
	@Override public boolean isInitial() {
		return isInitial;
	}
	@Override public boolean isFinal() {
		return isFinal;
	}
	@Override public String getName() {
		return name;
	}
	// NaturalId, also good for Id
	@Override public String getFullyQualifiedName() {
		return Utils.makeFullyQualifiedStateName(StateMachineProcessDefinition.EXAMPLE_SM_PROCESS.getProcessDefinitionId(), getName());
	}
}
enum ExampleSmProcessRoles implements ProcessRole {
    ROLE1,
    ROLE2;
}
enum ExampleSmTransitions implements Task, StateMachineTransition {
    TASK1(	"TASK1",
    		ExampleSmStates.STATE1, 
    		ExampleSmStates.STATE2, 
    		Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE1)
    ), 
    TASK2(	"TASK2",
    		ExampleSmStates.STATE2, 
    		ExampleSmStates.STATE3, 
    		Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE2)
    );
    private final Iterable<ProcessRole> roles;
    private final StateMachineState from;
    private final StateMachineState to;
    private final String name;
    ExampleSmTransitions(String name, StateMachineState from, StateMachineState to, Iterable<ProcessRole> roles) {
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