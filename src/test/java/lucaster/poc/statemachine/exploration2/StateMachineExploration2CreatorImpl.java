package lucaster.poc.statemachine.exploration2;

class CreatorImpl implements Creator {
	private final ProcessTopologyQuery topoQuery;
	private final ProcessIntegrationRepository integrationRepo;
	CreatorImpl(ProcessTopologyQuery topoQuery, ProcessIntegrationRepository integrationRepo) {
		this.topoQuery = topoQuery;
		this.integrationRepo = integrationRepo;
	}
	@Override
	public ProcessInstance createProcessInstance(String username, String processDefinitionId, String appInstanceId) {
		ProcessDefinition pd = topoQuery.findProcessDefinition(processDefinitionId);
		ProcessInstance pi = pd.createProcessInstance();
		integrationRepo.addIntegrationInfo(pi, appInstanceId); // TODO side-effect; use domain events?		
		return pi;
	}
}
