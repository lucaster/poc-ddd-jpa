package lucaster.poc.statemachine.exploration2;

interface ProcessIntegrationRepository {
	ProcessInstance findProcessInstanceByAppInstanceId(String appInstanceId);
	void updateIntegrationInfo(ProcessInstance pi, String appInstanceId);
	void addIntegrationInfo(ProcessInstance pi, String appInstanceId);
}

/**
 * For when processes will be data-driven.
 */
class StateMachineDataDrivenIntegration {
    final ProcessInstance pi;
    final String appInstanceId;
    protected Long versionForOptimisticLocking;
    StateMachineDataDrivenIntegration(ProcessInstance pi, String appInstanceId) {
        this.pi = pi;
        this.appInstanceId = appInstanceId;
    }
}

/**
 * For without data-driven
 */
class StateMachineSimpleIntegration {
	final String appInstanceId;
	final String processInstanceId;
	final String processDefinitionId;
	private String activeStateName;
	private String activeStateFullyQualifiedName; // FQ così è già univoco e quando passeremo a data-driven potremo sostituirlo col guid e tenere i nomi degli stati semplici
	protected Long versionForOptimisticLocking;
	public StateMachineSimpleIntegration(String appInstanceId, String processInstanceId, String processDefinitionId, String activeStateName) {
		this.appInstanceId = appInstanceId;
		this.processInstanceId = processInstanceId;
		this.processDefinitionId = processDefinitionId;
		this.activeStateName = activeStateName;
		this.setActiveStateFullyQualifiedName(Utils.makeFullyQualifiedStateName(processDefinitionId, activeStateName));
	}
	public void setActiveState(StateMachineState activeState) {
		this.activeStateName = activeState.getName();
		this.setActiveStateFullyQualifiedName(Utils.makeFullyQualifiedStateName(processDefinitionId, activeStateName));
	}
	String getActiveStateName() {
		return activeStateName;
	}
	public String getActiveStateFullyQualifiedName() {
		return activeStateFullyQualifiedName;
	}
	private void setActiveStateFullyQualifiedName(String activeStateFullyQualifiedName) {
		this.activeStateFullyQualifiedName = activeStateFullyQualifiedName;
	}
}