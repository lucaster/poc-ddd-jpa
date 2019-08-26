package lucaster.poc.statemachine.exploration2;

interface Creator {
	ProcessInstance createProcessInstance(String username, String processDefinitionId, String appInstanceId);
}
