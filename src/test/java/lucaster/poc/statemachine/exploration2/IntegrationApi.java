package lucaster.poc.statemachine.exploration2;

interface IntegrationRepository {
	ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId);
}

/**
 * Links Process Instance and app-specific data
 * @author Luca Cavagnoli
 *
 */
class AppProcInst {
	// PROCESS_INSTANCE_ID
    final ProcessInstance pi;
    // APP_INSTANCE_ID
    final String appInstanceId;
    protected Long versionForOptimisticLocking;
    AppProcInst(ProcessInstance pi, String appInstanceId) {
        this.pi = pi;
        this.appInstanceId = appInstanceId;
    }
}

class StateMachineSimpleIntegration {
	final String appInstanceId;
	final String processInstanceId;
	final String processDefinitionId;
	final String activeStateName;
	final String activeStateFullyQualifiedName; // FQ così è già univoco per quando passeremo a data-driven
	protected Long versionForOptimisticLocking;
	public StateMachineSimpleIntegration(String appInstanceId, String processInstanceId, String processDefinitionId, String activeStateName) {
		this.appInstanceId = appInstanceId;
		this.processInstanceId = processInstanceId;
		this.processDefinitionId = processDefinitionId;
		this.activeStateName = activeStateName;
		this.activeStateFullyQualifiedName = Utils.makeFullyQualifiedStateName(processDefinitionId, activeStateName);
	}
}