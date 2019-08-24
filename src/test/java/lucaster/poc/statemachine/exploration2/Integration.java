package lucaster.poc.statemachine.exploration2;

/**
 * Links Process Instance and app-specific data
 * @author Luca Cavagnoli
 *
 */
abstract class AppProcInst {
    final ProcessInstance pi;
    final String appInstanceId;
    AppProcInst(ProcessInstance pi, String appInstanceId /* + active state if FSM or active tasks if BPMN */) {
        this.pi = pi;
        this.appInstanceId = appInstanceId;
    }
}

class AppSmInst extends AppProcInst {
	private StateMachineState activeState;
	protected Long versionForOptimisticLocking;
	AppSmInst(StateMachineInstance pi, String appInstanceId, StateMachineState activeState) {
		super(pi, appInstanceId);
		this.setActiveState(activeState);
	}
	StateMachineState getActiveState() {
		return activeState;
	}
	void setActiveState(StateMachineState activeState) {
		this.activeState = activeState;
	}
}