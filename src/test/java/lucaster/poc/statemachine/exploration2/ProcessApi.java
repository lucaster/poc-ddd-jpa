package lucaster.poc.statemachine.exploration2;

interface ProcessDefinition {
	String getProcessDefinitionId();
	Task findTaskByName(String taskName);
}

interface Task {
	String getTaskName();
	Iterable<ProcessRole> getAllowedRoles();
}

interface ProcessRole {
}

interface ProcessInstance {
	ProcessDefinition getProcessDefinition();
	Iterable<Task> getActiveTasks();
}
