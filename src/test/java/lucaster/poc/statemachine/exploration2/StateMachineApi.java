package lucaster.poc.statemachine.exploration2;

// TODO YAGNI?
interface StateMachineDefinition extends ProcessDefinition {
	Iterable<StateMachineState> getStates();
	Iterable<Task> getOutgoingTransitions(StateMachineState state);
}

interface StateMachineState {
	boolean isFinal();
}

interface StateMachineTransition {
	StateMachineState getFrom();
	StateMachineState getTo();
}
