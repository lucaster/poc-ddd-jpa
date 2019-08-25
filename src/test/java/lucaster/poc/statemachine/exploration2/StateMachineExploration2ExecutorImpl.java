package lucaster.poc.statemachine.exploration2;

class ExecutorImpl implements Executor {
	private final ExecutorQuery query;
	private final ProcessIntegrationRepository integrationRepo;
	ExecutorImpl(ExecutorQuery query, ProcessIntegrationRepository integrationRepo) {
		this.query = query;
		this.integrationRepo = integrationRepo;
	}
	@Override public void execute(String appInstanceId, String taskName) {
		ProcessInstance pi = query.findProcessInstanceByAppInstanceId(appInstanceId);
		Task task = query.findTask(pi, taskName);
		pi.executeTask(task);
		integrationRepo.updateIntegrationInfo(pi, appInstanceId);
	}	
}

class ExecutorQueryImpl implements ExecutorQuery {
	
	private final ProcessIntegrationRepository integrationRepository;
	private final ProcessTopologyQuery procTopoQuery;
	
	ExecutorQueryImpl(ProcessIntegrationRepository integrationRepository, ProcessTopologyQuery topoQuery) {
		this.integrationRepository = integrationRepository;
		this.procTopoQuery = topoQuery;
	}

	@Override
	public ProcessInstance findProcessInstanceByAppInstanceId(String appInstanceId) {
		return integrationRepository.findProcessInstanceByAppIntanceId(appInstanceId);
	}

	@Override
	public Task findTask(ProcessInstance pi, String taskName) {
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		Task task = procTopoQuery.findTask(pd, taskName);
		return task;
	}
	
}