package lucaster.poc.statemachine.exploration2;

class CreatorImpl implements Creator {
	private final ProcessTopologyQuery topoQuery;
	CreatorImpl(ProcessTopologyQuery topoQuery) {
		this.topoQuery = topoQuery;
	}
	@Override
	public ProcessInstance createProcessInstance(String username, String processDefinitionId, String appInstanceId) {
		ProcessDefinition pd = topoQuery.findProcessDefinition(processDefinitionId);
		ProcessInstance pi = pd.createProcessInstance();
		return pi;
	}
}
