package lucaster.poc.statemachine.exploration2;

interface IntegrationRepository {
	ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId);
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
	final String activeStateName;
	final String activeStateFullyQualifiedName; // FQ così è già univoco e quando passeremo a data-driven potremo sostituirlo col guid o tener la natural key
	protected Long versionForOptimisticLocking;
	public StateMachineSimpleIntegration(String appInstanceId, String processInstanceId, String processDefinitionId, String activeStateName) {
		this.appInstanceId = appInstanceId;
		this.processInstanceId = processInstanceId;
		this.processDefinitionId = processDefinitionId;
		this.activeStateName = activeStateName;
		this.activeStateFullyQualifiedName = Utils.makeFullyQualifiedStateName(processDefinitionId, activeStateName);
	}
}