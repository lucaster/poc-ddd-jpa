package lucaster.poc.statemachine.exploration2;

class ExecutorImpl implements Executor {
	private final ExecutorQuery query;
	private final ProcessIntegrationRepository integrationRepo;
	private final Usher usher;
	ExecutorImpl(ExecutorQuery query, ProcessIntegrationRepository integrationRepo, Usher usher) {
		this.query = query;
		this.integrationRepo = integrationRepo;
		this.usher = usher;
	}
	@Override public void execute(String username, String appInstanceId, String taskName) {
		if (!usher.canExecute(username, taskName, appInstanceId)) {
			throw new RuntimeException(new IllegalStateException());
		}
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