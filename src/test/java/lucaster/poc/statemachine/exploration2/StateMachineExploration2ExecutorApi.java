package lucaster.poc.statemachine.exploration2;

interface Executor {
	void execute(String username, String appInstanceId, String taskName); 
}

interface ExecutorQuery {
	ProcessInstance findProcessInstanceByAppInstanceId(String appInstanceId);
	Task findTask(ProcessInstance processInstance, String taskName);
}
