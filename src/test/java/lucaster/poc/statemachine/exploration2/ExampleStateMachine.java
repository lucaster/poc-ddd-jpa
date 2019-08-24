package lucaster.poc.statemachine.exploration2;

enum ExampleSmProcessStates implements StateMachineState {
	STATE1(false),
	STATE2(false),
	STATE3(true);
	private final boolean isFinal;
	ExampleSmProcessStates(boolean isFinal) {
		this.isFinal = isFinal;
	}
	@Override public boolean isFinal() {
		return isFinal;
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